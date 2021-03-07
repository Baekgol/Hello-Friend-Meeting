<template>
  <div class="pa-5">
    <button @click="goToNaegi">내기페이지로 이동</button>
    <v-row>
      <v-col cols="12" md="6" class="mb-4">
        <v-row>
          <v-col cols="2" align="center">
            <v-icon @click="preMonth">mdi-chevron-left</v-icon>
          </v-col>
          <v-col cols="8" align="center">
            <h3 align="center">{{ curYM }}</h3>
          </v-col>
          <v-col cols="2" align="center">
            <v-icon @click="nextMonth">mdi-chevron-right</v-icon>
          </v-col>
        </v-row>
        <v-sheet height="500">
          <v-calendar
            :event-color="getEventColor"
            :events="localEvents"
            :start="start"
            :type="type"
            @click:date="open"
            @click:event="showEvent"
            @click:more="moreEvent"
            @click:time="open"
            ref="calendar"
            v-model="start"
          ></v-calendar>
          <!---->
          <v-dialog
            v-model="selectedOpen"
            fullscreen
            hide-overlay
            transition="dialog-bottom-transition"
          >
            <v-card>
              <v-toolbar dark :color="selectedEvent.color">
                <v-toolbar-title>{{ selectedEvent.name }}</v-toolbar-title>
                <v-spacer></v-spacer>
                <v-icon
                  v-if="selectedEvent.smasterEmail === myEmail"
                  @click="delSchedule"
                  >mdi-trash-can</v-icon
                >
                <v-icon @click="closeSchedule">mdi-close</v-icon>
              </v-toolbar>
              <v-list three-line subheader>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-title>{{
                      selectedEvent.content
                    }}</v-list-item-title>
                    <v-list-item-subtitle
                      >{{ selectedEvent.start }} ~
                      {{ selectedEvent.end }}</v-list-item-subtitle
                    >
                  </v-list-item-content>
                </v-list-item>
              </v-list>
              <v-divider></v-divider>

              <div>
                <v-subheader>참가자</v-subheader>
                <span
                  v-for="participant in selectedEvent.participants"
                  :key="participant.spno"
                >
                  <v-chip
                    v-if="participant.attendance === 1"
                    class="ma-2"
                    :color="participant.color"
                    text-color="white"
                  >
                    {{ participant.name }}
                  </v-chip>
                </span>
                <v-subheader>미참가자</v-subheader>

                <span
                  v-for="participant in selectedEvent.participants"
                  :key="participant.spno"
                >
                  <v-chip
                    v-if="participant.attendance === 2"
                    class="ma-2"
                    :color="participant.color"
                    text-color="white"
                  >
                    {{ participant.name }}
                  </v-chip>
                </span>
                <v-subheader>미응답</v-subheader>
                <span
                  v-for="participant in selectedEvent.participants"
                  :key="participant.spno"
                >
                  <v-chip
                    v-if="participant.attendance === 0"
                    class="ma-2"
                    :color="participant.color"
                    text-color="white"
                  >
                    {{ participant.name }}
                  </v-chip>
                </span>
                <v-divider></v-divider>

                <div>
                  <h2>참가 하실건가요?</h2>
                  <br />
                  <v-btn
                    depressed
                    color="primary"
                    style="width:50%"
                    @click="voteAttendance(1)"
                  >
                    참가
                  </v-btn>
                  <v-btn
                    depressed
                    color="error"
                    style="width:50%"
                    @click="voteAttendance(2)"
                  >
                    다음에..
                  </v-btn>
                </div>
              </div>
            </v-card>
          </v-dialog>
        </v-sheet>
      </v-col>
    </v-row>
    <v-dialog v-model="moreEventsDialog" width="500">
      <v-card>
        <v-card-title class="headline grey lighten-2">
          {{ moreDate }}
        </v-card-title>

        <v-card-text>
          <v-chip
            v-for="event in moreEvents"
            :key="event.sno"
            @click="showEventAtMore(event)"
            class="ma-2"
            :color="event.color"
            text-color="white"
          >
            <span class=" text-truncate">
              {{ event.name }}
            </span>
          </v-chip>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" text @click="moreEventsDialog = false">
            close
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <Dialog />
  </div>
</template>

<script>
import Dialog from "@/components/group/Dialog.vue";
import axios from "axios";

const colors = [
  "#B0E0E6",
  "green",
  "orange",
  "#FFC0CB",
  "#808000",
  "#ABC88B",
  "#DDA0DD",
  "#00FFFF",
];
export default {
  components: {
    Dialog,
  },
  computed: {
    vuexGno() {
      return this.$store.getters.getGno;
    },
    vuexUno() {
      return this.$store.getters.getUno;
    },
    vuexBno() {
      return this.$store.getters.getBno;
    },
    vuexMemberStatus() {
      return this.$store.getters.getMemberStatus;
    },
    events() {
      return this.$store.getters.getEvents;
    },
    eventsNum() {
      return this.$store.getters.getEvents.length;
    },
  },
  watch: {
    vuexGno(val) {
      this.gno = val;
      const today = new Date();
      const year = today.getFullYear();
      const month = today.getMonth();
      const date = today.getDate();
      const startDate = `${year}-${month}-${date}`;
      this.curYear = year;
      this.curMonth = month + 1;
      if (this.curMonth < 10) this.curMonth = "0" + this.curMonth;
      this.start = startDate;
      this.getSchedules();
    },
    vuexUno(val) {
      this.uno = val;
    },
    vuexBno(val) {
      this.bno = val;
    },
    vuexMemberStatus(val) {
      this.memberStatus = val;
    },
    events(val) {
      this.localEvents = val;
      // this.getSchedules(); //led에디션
    },
    eventsNum(val) {
      this.getSchedules();
    },
    curMonth() {
      this.start = `${this.curYear}-${this.curMonth}-01`;
      this.curYM = `${this.curYear} . ${this.curMonth}`;
    },
  },
  created() {
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth();
    const date = today.getDate();
    const startDate = `${year}-${month}-${date}`;
    this.curYear = year;
    this.curMonth = month + 1;
    if (this.curMonth < 10) this.curMonth = "0" + this.curMonth;
    this.start = startDate;
    this.getSchedules();
  },

  data() {
    return {
      localEvents: [],
      openedDialog: {
        sno: { type: Number },
        sgno: { type: Number },
        smaster: { type: Number },
        senddate: "",
        sstartdate: "",
        stitle: "",
        sspace: "",
        scontetnt: "",
      },
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      bno: this.$store.getters.getBno,
      uno: this.$store.getters.getUno,
      dateOpen: false,
      start: this.startDate,
      type: "month",
      curYear: this.year,
      curMonth: this.month + 1,
      curYM: "",
      selectedEvent: {},
      selectedElement: null,
      selectedOpen: false,
      myEmail: window.sessionStorage.getItem("user-email"),
      moreEvents: "",
      moreEventsDialog: false,
      moreDate: "",
    };
  },
  methods: {
    preMonth() {
      this.curMonth *= 1;
      this.curMonth--;
      if (this.curMonth === 0) {
        this.curMonth = 12;
        this.curYear *= 1;
        this.curYear--;
        this.curYear += "";
      }
      this.curMonth += "";
      if (this.curMonth.length === 1) this.curMonth = "0" + this.curMonth;
    },
    nextMonth() {
      this.curMonth *= 1;
      this.curMonth++;
      if (this.curMonth === 13) {
        this.curMonth = 1;
        this.curYear *= 1;
        this.curYear++;
        this.curYear += "";
      }
      this.curMonth += "";
      if (this.curMonth.length === 1) this.curMonth = "0" + this.curMonth;
    },
    open(date) {
      console.log(date);
      this.$store.commit("OPEN_CALENDAR_DIALOG", date);
    },
    getEventColor(event) {
      return event.color;
    },
    showEvent({ nativeEvent, event }) {
      var params = new URLSearchParams();
      params.append("sno", event.sno);
      axios.post("getScheduleParticipants", params).then((resp) => {
        var len = resp.data.list.length;
        var participantList = [];

        for (var i = 0; i < len; i++) {
          var participantInfo = resp.data.list[i];
          participantInfo.email = resp.data.emailList[i];
          participantInfo.name = resp.data.nameList[i];
          (participantInfo.color = colors[Math.floor(Math.random() * 8)]),
            participantList.push(participantInfo);
        }
        const open = () => {
          this.selectedEvent = event;
          this.selectedElement = nativeEvent.target;
          setTimeout(() => {
            this.selectedOpen = true;
          }, 10);
        };

        if (this.selectedOpen) {
          this.selectedOpen = false;
          setTimeout(open, 10);
        } else open();
        nativeEvent.stopPropagation();
        this.selectedEvent.participants = participantList;
        console.log(this.selectedEvent);
      });
    },
    showEventAtMore(event) {
      var params = new URLSearchParams();
      params.append("sno", event.sno);
      axios.post("getScheduleParticipants", params).then((resp) => {
        var len = resp.data.list.length;
        var participantList = [];
        for (var i = 0; i < len; i++) {
          var participantInfo = resp.data.list[i];
          participantInfo.email = resp.data.emailList[i];
          participantInfo.name = resp.data.nameList[i];
          participantList.push(participantInfo);
        }
        const open = () => {
          this.selectedEvent = event;
          setTimeout(() => {
            this.selectedOpen = true;
          }, 10);
        };
        if (this.selectedOpen) {
          this.selectedOpen = false;
          setTimeout(open, 10);
        } else open();
        this.selectedEvent.participants = participantList;
      });
    },
    moreEvent(date) {
      console.log(date.date);
      this.moreDate = date.date + "의 일정은??";
      console.log(this.localEvents);
      this.moreEvents = [];
      var selectedDate = this.returnToNum(date.date);
      for (var i = 0; i < this.localEvents.length; i++) {
        if (
          this.returnToNum(this.localEvents[i].start.split(" ")[0]) <=
            selectedDate &&
          this.returnToNum(this.localEvents[i].end.split(" ")[0]) >=
            selectedDate
        ) {
          this.moreEvents.push(this.localEvents[i]);
        }
      }
      console.log(this.moreEvents);
      this.moreEventsDialog = true;
    },
    getSchedules() {
      var params = new URLSearchParams();
      params.append("gno", this.gno);
      axios.post("getSchedulesList", params).then((resp) => {
        if (resp.data.isPresent) {
          var eventList = resp.data.list;
          var emailList = resp.data.emailList;
          for (var i = 0; i < eventList.length; i++)
            eventList[i].smasterEmail = emailList[i];
          this.$store.commit("ADD_EVENTS", resp.data.list);
        }
      });
    },
    delSchedule() {
      console.log(this.selectedEvent);
      var params = new URLSearchParams();
      params.append("sno", this.selectedEvent.sno);
      axios.post("delSchedule", params).then(() => {
        this.getSchedules();
        this.selectedOpen = false;
        alert("일정을 삭제했습니다.");
      });
      this.moreEventsDialog = false;
    },
    closeSchedule() {
      this.selectedOpen = false;
    },
    returnToNum(date) {
      var toReturn = date.replace(/-/g, "");
      toReturn *= 1;
      return toReturn;
    },
    voteAttendance(attendance) {
      var params = new URLSearchParams();
      params.append("sno", this.selectedEvent.sno);
      params.append("email", this.myEmail);
      params.append("attendance", attendance);
      axios.post("changeScheduleAttendance", params).then((resp) => {
        if (resp.data.isPresent) {
          axios.post("getScheduleParticipants", params).then((resp) => {
            var len = resp.data.list.length;
            var participantList = [];

            for (var i = 0; i < len; i++) {
              var participantInfo = resp.data.list[i];
              participantInfo.email = resp.data.emailList[i];
              participantInfo.name = resp.data.nameList[i];
              participantInfo.color = colors[Math.floor(Math.random() * 8)];
              participantList.push(participantInfo);
            }
            // this.selectedOpen = false;
            var tmp = this.selectedEvent;
            this.selectedEvent = null;
            this.selectedEvent = tmp;
            this.selectedEvent.participants = participantList;
          });
        }
      });
    },
    goToNaegi() {
      this.$router.push("/naegi");
    },
  },
};
</script>
