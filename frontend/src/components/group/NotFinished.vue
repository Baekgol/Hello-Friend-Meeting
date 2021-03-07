<template>
  <!-- 그룹이 존재할 경우 -->
  <v-list
    v-if="notFinishedList.length != 0"
    height="639"
    style="overflow-y: auto;"
    two-line
  >
    <template v-for="(naegi, index) in notFinishedList">
      <v-divider
        v-if="index == 0"
        :key="'dividerUp' + index"
        style="margin-top: 0;"
      ></v-divider>

      <v-list-item :key="naegi.nno" @click="openDialog(naegi)">
        <v-list-item-avatar> </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title v-text="naegi.ntitle"></v-list-item-title>
          <v-list-item-subtitle v-text="naegi.ncontent"></v-list-item-subtitle>
          <v-list-item-subtitle>{{ naegi.remainTime }}</v-list-item-subtitle>
        </v-list-item-content>

        <v-list-item-icon> </v-list-item-icon>
      </v-list-item>

      <v-divider :key="'dividerDown' + index"></v-divider>
    </template>
    <v-row justify="center">
      <v-dialog
        v-model="dialog"
        fullscreen
        hide-overlay
        transition="dialog-bottom-transition"
      >
        <v-card>
          <v-toolbar dark color="deep-purple accent-4">
            <v-toolbar-title>{{ curNaegi.ntitle }}</v-toolbar-title>
            <v-spacer></v-spacer
            ><v-icon v-if="curNaegi.email === myEmail" @click="delNaegi"
              >mdi-trash-can</v-icon
            >
            <v-btn icon dark @click="dialog = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-toolbar>
          <v-list three-line subheader>
            <v-list-item>
              <v-list-item-content>
                <v-list-item-title>{{ curNaegi.ncontent }}</v-list-item-title>
                <v-list-item-subtitle
                  >상품 : {{ curNaegi.nreward }}</v-list-item-subtitle
                ><v-list-item-subtitle
                  >만료시간 : {{ curNaegi.nenddate }}</v-list-item-subtitle
                >
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <div v-if="myVote === 0">
            <v-list three-line subheader>
              <v-subheader>참여인원</v-subheader>
              <v-list-item
                ><v-list-item-content
                  ><v-list-item-title
                    >{{ notVoteParticipants + voteParticipants }}명중
                    {{ voteParticipants }}명이 참여했습니다.</v-list-item-title
                  ></v-list-item-content
                ></v-list-item
              >
            </v-list>
            <v-divider></v-divider>
            <div>
              <h2>나의 선택은?</h2>
              <br />
              <v-btn
                depressed
                color="primary"
                style="width:50%"
                @click="voteAttendance(1)"
              >
                성공
              </v-btn>
              <v-btn
                depressed
                color="error"
                style="width:50%"
                @click="voteAttendance(2)"
              >
                실패
              </v-btn>
            </div>
          </div>
          <div v-else>
            <v-subheader>성공에 건사람</v-subheader>
            <span
              v-for="participant in curNaegi.participants"
              :key="participant.npno"
              ><v-chip
                v-if="participant.nattendance === 1"
                class="ma-2"
                :color="participant.color"
                text-color="white"
              >
                {{ participant.name }}
              </v-chip>
            </span>
            <v-subheader>실패에 건사람</v-subheader>
            <span
              v-for="participant in curNaegi.participants"
              :key="participant.npno"
              ><v-chip
                v-if="participant.nattendance === 2"
                class="ma-2"
                :color="participant.color"
                text-color="white"
              >
                {{ participant.name }}
              </v-chip>
            </span>
            <v-subheader>미참여</v-subheader>
            <span
              v-for="participant in curNaegi.participants"
              :key="participant.npno"
              ><v-chip
                v-if="participant.nattendance === 0"
                class="ma-2"
                :color="participant.color"
                text-color="white"
              >
                {{ participant.name }}
              </v-chip></span
            >
          </div>
          <div v-if="curNaegi.email === myEmail && myVote != 0">
            <v-divider></v-divider>
            <h1>
              결과는??<v-btn
                rounded
                color="primary"
                dark
                style="float:right"
                @click="changeResult"
                >GO!</v-btn
              >
            </h1>
            <b-form-radio-group
              id="btn-radios-2"
              v-model="selected"
              :options="options"
              button-variant="outline-primary"
              size="lg"
              name="radio-btn-outline"
              buttons
              style="width:100%"
            ></b-form-radio-group>
          </div>
        </v-card>
      </v-dialog>
    </v-row>
  </v-list>

  <!-- 그룹이 존재하지 않을 경우 -->
  <v-row v-else class="ma-0" style="height: 639px;" justify="center">
    <v-icon color="red" size="100">mdi-close-circle</v-icon>
  </v-row>
</template>

<script>
import axios from 'axios';

const colors = [
  '#B0E0E6',
  'green',
  'orange',
  '#FFC0CB',
  '#808000',
  '#ABC88B',
  '#DDA0DD',
  '#00FFFF',
];

export default {
  props: ['notFinishedList'],
  data() {
    return {
      dialog: false,
      curNaegi: {},
      myVote: 4,
      voteParticipants: 0,
      notVoteParticipants: 0,
      myEmail: window.sessionStorage.getItem('user-email'),
      selected: 1,
      options: [
        { text: '성공', value: 1 },
        { text: '실패', value: 2 },
        { text: '무승부', value: 3 },
      ],
    };
  },
  methods: {
    openDialog(naegi) {
      this.notVoteParticipants = 0;
      this.voteParticipants = 0;

      var params = new URLSearchParams();
      params.append('nno', naegi.nno);
      this.curNaegi = naegi;

      axios.post('/getNaegiParticipants', params).then((resp) => {
        if (resp.data.isPresent) {
          var emailList = resp.data.emailList;
          var nameList = resp.data.nameList;
          var participants = resp.data.list;
          var len = nameList.length;

          for (var i = 0; i < len; i++) {
            participants[i].email = emailList[i];
            participants[i].name = nameList[i];
            participants[i].color = colors[Math.floor(Math.random() * 8)];
            if (window.sessionStorage.getItem('user-email') === emailList[i])
              this.myVote = participants[i].nattendance;
            if (participants[i].nattendance === 0) this.notVoteParticipants++;
            else this.voteParticipants++;
          }

          this.curNaegi.participants = participants;
          this.curNaegi.email = resp.data.masterEmail;
          console.log(this.curNaegi);
          this.dialog = true;
        }
      });
    },
    voteAttendance(attendance) {
      var params = new URLSearchParams();
      params.append('nno', this.curNaegi.nno);
      params.append('email', window.sessionStorage.getItem('user-email'));
      params.append('attendance', attendance);

      axios.post('changeNaegiAttendance', params).then((response) => {
        if (response.data.isPresent) {
          params = new URLSearchParams();
          params.append('nno', this.curNaegi.nno);

          axios.post('getNaegiParticipants', params).then((resp) => {
            if (resp.data.isPresent) {
              var emailList = resp.data.emailList;
              var nameList = resp.data.nameList;
              var participants = resp.data.list;
              var len = nameList.length;

              for (var i = 0; i < len; i++) {
                participants[i].email = emailList[i];
                participants[i].name = nameList[i];
                participants[i].color = colors[Math.floor(Math.random() * 8)];
                if (
                  window.sessionStorage.getItem('user-email') === emailList[i]
                )
                  this.myVote = participants[i].nattendance;
              }

              this.curNaegi.participants = participants;
              console.log(this.curNaegi);
              this.myVote = attendance;
              alert(response.data.data);
            }
          });
        }
      });
    },
    changeResult() {
      console.log(this.selected);
      var params = new URLSearchParams();
      params.append('nno', this.curNaegi.nno);
      params.append('result', this.selected);
      axios.post('changeNaegiResult', params).then((response) => {
        if (response.data.isPresent) {
          params = new URLSearchParams();
          params.append('gno', this.curNaegi.ngno);
          axios.post('getNotFinishedNaegis', params).then((resp) => {
            if (resp.data.isPresent) {
              this.notFinishedList = resp.data.naegiList;
              var len = this.notFinishedList.length;

              for (var i = 0; i < len; i++) {
                this.notFinishedList[i].remainTime = resp.data.remainTime[i];
                this.notFinishedList[i].color =
                  colors[Math.floor(Math.random() * 8)];
              }
              console.log(this.notFinishedList);
              this.dialog = false;

              this.selected = 1;
              alert(response.data.data);
            }
          });
        }
      });
    },
    delNaegi() {
      var params = new URLSearchParams();
      params.append('nno', this.curNaegi.nno);
      axios.post('delNaegi', params).then((response) => {
        if (response.data.isPresent) {
          params = new URLSearchParams();
          params.append('gno', this.curNaegi.ngno);
          axios.post('getNotFinishedNaegis', params).then((resp) => {
            if (resp.data.isPresent) {
              this.notFinishedList = resp.data.naegiList;
              var len = this.notFinishedList.length;

              for (var i = 0; i < len; i++) {
                this.notFinishedList[i].remainTime = resp.data.remainTime[i];
                this.notFinishedList[i].color =
                  colors[Math.floor(Math.random() * 8)];
              }
            } else {
              this.notFinishedList = [];
            }
            alert(response.data.data);
            this.dialog = false;
          });
        }
      });
    },
  },
};
</script>

<style></style>
