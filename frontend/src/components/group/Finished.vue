<template>
  <!-- 그룹이 존재할 경우 -->
  <v-list
    v-if="finishedList.length != 0"
    height="639"
    style="overflow-y: auto;"
    two-line
  >
    <template v-for="(naegi, index) in finishedList">
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
                  >종료시간 : {{ curNaegi.nenddate }}</v-list-item-subtitle
                >
              </v-list-item-content>
            </v-list-item>
          </v-list>
          <v-divider></v-divider>
          <v-list three-line subheader>
            <div v-if="curNaegi.nresult === 1 || curNaegi.nresult === 2">
              <v-subheader>맞춘사람</v-subheader>
              <v-chip
                class="ma-2"
                v-for="participant in curNaegi.participants"
                :color="participant.color"
                text-color="white"
                :key="participant.npno"
                v-show="
                  participant.nattendance === curNaegi.nresult &&
                    curNaegi.nresult != 0 &&
                    curNaegi.nresult != 3
                "
                >{{ participant.name }}</v-chip
              >
            </div>
            <div v-if="curNaegi.nresult === 0 || curNaegi.nresult === 3">
              <v-subheader>취소된 내기입니다.</v-subheader>
            </div>
          </v-list>
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
  props: ['finishedList'],
  data() {
    return {
      dialog: false,
      curNaegi: {},
      myEmail: window.sessionStorage.getItem('user-email'),
    };
  },
  created() {},
  methods: {
    openDialog(naegi) {
      var params = new URLSearchParams();
      params.append('nno', naegi.nno);

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
          }
          this.curNaegi = naegi;
          this.curNaegi.email = resp.data.masterEmail;
          this.curNaegi.participants = participants;
          this.dialog = true;
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
          axios.post('getFinishedNaegis', params).then((resp) => {
            if (resp.data.isPresent) {
              this.finishedList = resp.data.naegiList;
            } else {
              this.finishedList = [];
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
