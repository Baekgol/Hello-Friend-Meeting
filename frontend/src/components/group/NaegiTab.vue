<template>
  <v-container class="pa-0">
    <v-row class="ma-0">
      <v-tabs
        v-model="tab"
        background-color="#212034"
        color="#E2B9C6"
        centered
        dark
        grow
        icons-and-text
      >
        <v-tabs-slider></v-tabs-slider>

        <v-tab href="#tab-1" replace>
          진행중인 내기
          <v-icon>mdi-text-box-outline</v-icon>
        </v-tab>

        <v-tab href="#tab-2" replace>
          완료된 내기
          <v-icon>mdi-text-box-check-outline</v-icon>
        </v-tab>

        <v-tabs-items v-model="tab">
          <v-icon @click="goToGroup">mdi-arrow-left</v-icon>
          <v-btn
            v-b-modal.modal-1
            color="deep-purple accent-4"
            dark
            absolute
            bottom
            style="margin-bottom:56px"
            right
            fab
            ><v-icon>mdi-plus</v-icon></v-btn
          >
          <b-modal
            id="modal-1"
            title="내기추가"
            :ok-disabled="
              !isRightFormat || reward === '' || title === '' || content === ''
            "
            centered
            @ok="addNaegi"
            @hide="resetDatas"
          >
            제목 : <input type="text" v-model="title" /><br />
            보상 : <input type="text" v-model="reward" />
            <hr />
            내용 : <br />
            <b-form-textarea
              id="textarea-rows"
              rows="8"
              v-model="content"
            ></b-form-textarea>
            <v-row>
              <v-col cols="6" class="pb-0">
                <v-menu
                  ><template v-slot:activator="{ on }">
                    <v-text-field
                      slot="activator"
                      label="만료일"
                      readonly
                      prepend-icon="mdi-calendar-month"
                      v-on="on"
                      :value="startDate"
                      class=""
                    ></v-text-field>
                  </template>
                  <v-date-picker
                    :allowed-dates="allowedDates"
                    v-model="startDate"
                  ></v-date-picker>
                </v-menu>
              </v-col>
              <v-col cols="6" class="pb-0">
                <v-menu>
                  <template v-slot:activator="{ on }">
                    <v-text-field
                      label="만료 시간"
                      readonly
                      :value="endTime"
                      prepend-icon="mdi-timer"
                      v-on="on"
                    ></v-text-field>
                  </template>
                  <v-time-picker v-model="endTime">
                    <v-btn class="mx-auto">선택 </v-btn>
                  </v-time-picker>
                </v-menu>
              </v-col>
            </v-row>
          </b-modal>
          <v-tab-item style="clear:both" :value="'tab-1'">
            <v-row
              v-if="isLoadingNotFinished"
              class="ma-0"
              style="height: 639px;"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="purple"
              ></v-progress-circular>
            </v-row>

            <not-finished
              v-if="!isLoadingNotFinished"
              :notFinishedList="notFinishedList"
            />
          </v-tab-item>

          <v-tab-item style="clear:both" :value="'tab-2'">
            <v-row
              v-if="isLoadingFinished"
              class="ma-0"
              style="height: 639px;"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="purple"
              ></v-progress-circular>
            </v-row>

            <finished v-if="!isLoadingFinished" :finishedList="finishedList" />
          </v-tab-item>
        </v-tabs-items>
      </v-tabs>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import Finished from '@/components/group/Finished.vue';
import NotFinished from '@/components/group/NotFinished.vue';

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
  components: { Finished, NotFinished },
  data() {
    return {
      myEmail: window.sessionStorage.getItem('user-email'),
      tab: null,
      finishedList: [],
      notFinishedList: [],
      isLoadingFinished: true,
      isLoadingNotFinished: true,
      modalOpend: false,
      title: '',
      content: '',
      reward: '',
      startDate: '',
      endTime: '',
    };
  },
  created() {
    const today = new Date();
    const year = today.getFullYear();
    var month = today.getMonth() + 1;
    const date = today.getDate();
    if (month < 10) month = '0' + month;

    this.startDate = `${year}-${month}-${date}`;
    var hour = today.getHours();
    var minute = today.getMinutes();
    if (hour < 10) hour = '0' + hour;
    if (minute < 10) minute = '0' + minute;
    this.endTime = hour + ':' + minute;
    this.getNotFinished();
    this.getFinished();
  },
  methods: {
    allowedDates(val) {
      let endDate = val.split('-').reduce((a, b) => a + b);
      let startDate = this.startDate.split('-').reduce((a, b) => a + b);
      return endDate >= startDate;
    },
    resetDatas() {
      this.title = '';
      this.content = '';
      this.reward = '';

      const today = new Date();
      const year = today.getFullYear();
      var month = today.getMonth() + 1;
      const date = today.getDate();
      if (month < 10) month = '0' + month;
      this.startDate = `${year}-${month}-${date}`;
      var hour = today.getHours();
      var minute = today.getMinutes();
      if (hour < 10) hour = '0' + hour;
      if (minute < 10) minute = '0' + minute;
      this.endTime = hour + ':' + minute;
    },
    addNaegi() {
      var params = new URLSearchParams();
      params.append('email', this.myEmail);
      params.append('ntitle', this.title);
      params.append('ncontent', this.content);
      params.append('nreward', this.reward);
      params.append('nenddate', this.enddate + ':00');
      params.append('ngno', this.vuexGno);

      axios.post('addNaegi', params).then((resp) => {
        this.getNotFinished();
        this.getFinished();
        alert(resp.data.result);
      });
    },
    goToGroup() {
      this.$router.push('/group');
    },
    getNotFinished() {
      var params = new URLSearchParams();
      params.append('gno', this.vuexGno);
      axios.post('getNotFinishedNaegis', params).then((resp) => {
        if (resp.data.isPresent) {
          this.notFinishedList = resp.data.naegiList;
        }
        var len = this.notFinishedList.length;

        for (var i = 0; i < len; i++) {
          this.notFinishedList[i].remainTime = resp.data.remainTime[i];
        }
        this.isLoadingNotFinished = false;
        console.log(this.notFinishedList);
      });
    },
    getFinished() {
      var params = new URLSearchParams();
      params.append('gno', this.vuexGno);
      axios.post('getFinishedNaegis', params).then((resp) => {
        if (resp.data.isPresent) {
          this.finishedList = resp.data.naegiList;
        }
        this.isLoadingFinished = false;
        console.log(this.finishedList);
      });
    },
  },
  computed: {
    vuexGno() {
      return this.$store.getters.getGno;
    },
    isRightFormat() {
      return this.enddate.length === 16;
    },
    enddate() {
      return this.startDate + ' ' + this.endTime;
    },
  },
  watch: {
    tab(tab) {
      if (tab == 'tab-1') {
        this.isLoadingFinished = true;
        this.finishedList = [];
        this.getNotFinished();
      } else {
        this.notFinishedList = [];
        this.getFinished();
      }
    },
  },
};
</script>

<style></style>
