<template>
  <v-dialog max-width="600px" persistent v-model="dialog">
    <v-card>
      <v-card-title>
        <h3>일정 추가</h3>
      </v-card-title>
      <v-card-text>
        <v-form class="px-3" ref="form">
          <v-text-field
            label="일정"
            v-model="calendar.title"
            prepend-icon="mdi-folder-marker"
          ></v-text-field>
          <v-textarea
            label="상세설명"
            v-model="calendar.content"
            prepend-icon="mdi-pencil"
          ></v-textarea>
          <v-row>
            <v-col cols="6" class="pb-0">
              <v-menu>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    slot="activator"
                    label="시작일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="calendar.startDate"
                    class=""
                  ></v-text-field>
                </template>
                <v-date-picker v-model="calendar.startDate"></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-menu
                :close-on-content-click="false"
                v-model="startTimer"
                offset-y
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="시작 시간"
                    readonly
                    :value="calendar.startTime"
                    prepend-icon="mdi-timer"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker v-if="startTimer" v-model="calendar.startTime">
                  <v-btn class="mx-auto" @click="selectTime">선택 </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="6" class="pt-0">
              <v-menu>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    slot="activator"
                    label="종료일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="calendar.endDate"
                    class=""
                  ></v-text-field>
                </template>
                <v-date-picker
                  v-model="calendar.endDate"
                  :allowed-dates="allowedDates"
                ></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pt-0">
              <v-menu
                :close-on-content-click="false"
                v-model="endTimer"
                offset-y
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="종료 시간"
                    readonly
                    :value="calendar.endTime"
                    prepend-icon="mdi-timer"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker v-if="endTimer" v-model="calendar.endTime">
                  <v-btn class="mx-auto" @click="selectTime">선택 </v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <div class="text-center">
            <v-btn text class="primary white--text mx-2 mt-3" @click="submit">
              추가
            </v-btn>
            <v-btn text class="primary white--text mx-2 mt-3" @click="close">
              닫기
            </v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      startTimer: false,
      endTimer: false,
      localCalendar: {
        sno: 1,
        sgno: { type: Number },
        smaster: "",
        senddate: "",
        sstartdate: "",
        stitle: "",
        sspace: "",
        scontent: "",
      },
    };
  },
  computed: {
    dialog() {
      return this.$store.state.dialog;
    },
    calendar() {
      return this.$store.getters.getCalendar;
    },
  },
  methods: {
    setFormat() {
      this.localCalendar.sgno = this.$store.getters.getGno;
      this.localCalendar.senddate =
        this.calendar.endDate + " " + this.calendar.endTime + ":00";
      this.localCalendar.sstartdate =
        this.calendar.startDate + " " + this.calendar.startTime + ":00";
      this.localCalendar.stitle = this.calendar.title;
      this.localCalendar.scontent = this.calendar.content;
    },
    submit() {
      if (this.$refs.form.validate()) {
        this.setFormat();

        var params = new URLSearchParams();
        params.append("startDate", this.localCalendar.sstartdate);
        params.append("endDate", this.localCalendar.senddate);
        params.append("email", window.sessionStorage.getItem("user-email"));
        params.append("gno", this.$store.getters.getGno);
        params.append("title", this.localCalendar.stitle);
        params.append("content", this.localCalendar.scontent);

        axios.post("addSchedule", params).then((resp) => {
          this.$store.commit("ADD_EVENT", resp.data.createdSchedule);
          this.close();
        });
      }
    },
    close() {
      this.$store.commit("CLOSE_CALENDAR_DIALOG");
    },
    selectTime() {
      this.endTimer = false;
      this.startTimer = false;
    },
    allowedDates(val) {
      let endDate = val.split("-").reduce((a, b) => a + b);
      let startDate = this.calendar.startDate
        .split("-")
        .reduce((a, b) => a + b);
      return endDate >= startDate;
    },
  },
};
</script>
