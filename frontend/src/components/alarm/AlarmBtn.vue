<template>
  <div class="text-center">
    <v-menu v-model="menu" :close-on-content-click="false" offset-x>
      <template v-slot:activator="{ on, attrs }">
        <v-btn icon v-bind="attrs" v-on="on" color="white">
          <v-badge :value="alarmLen != 0" color="#804F69" right overlap>
            <span slot="badge">{{ alarmLen }}</span>
            <v-icon color="#E8E1D5">mdi-bell</v-icon>
          </v-badge>
        </v-btn>
      </template>

      <v-card>
        <v-list>
          <v-list-item>
            <v-list-item-avatar>
              <img :src="imgPath" />
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title>{{ userInfo.uname }}</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list>

        <v-divider style="margin: 0; border: dashed #D3D3D3 1px;"></v-divider>

        <v-list
          width="300"
          max-height="300"
          style="overflow-y: auto; padding: 0;"
          ><v-row
            v-if="alarms == null || alarms.length == 0"
            class="ma-0"
            align="center"
            justify="center"
            style="height: 70px;"
          >
            <v-col
              cols="2"
              align="center"
              style="padding: 0; margin-left: 30px; margin-top: -15px;"
            >
              <v-img
                src="@/assets/images/alarm.gif"
                style="width: 40px; height: 40px; "
              ></v-img>
            </v-col>
            <v-col
              style="letter-spacing: -1px; padding: 0; margin-left: -50px;"
            >
              <div style="font-size: 0.9rem; text-align: center;">
                뭐, 곧 새로운 알림이 올거예요.
              </div>
              <div style="font-size: 0.7rem; text-align: center;">
                *귀뚜라미 소리*
              </div>
            </v-col>
          </v-row>
          <v-row
            v-else
            no-gutters
            v-for="alarm in alarms"
            :key="alarm.ano"
            :style="{ backgroundColor: alarmBgColor(alarm.aisRead) }"
            style="margin: 0;"
          >
            <v-col cols="9" style="padding: 0;">
              <v-list-item
                @click="modalShowMethod(alarm)"
                style="padding-right: 0;"
              >
                <v-list-item-title style="letter-spacing: -1px;">
                  {{ alarm.asummary }}
                </v-list-item-title>
              </v-list-item>
            </v-col>
            <v-col style="margin: 0; padding: 0;"
              ><v-btn class="ma-2" text icon @click="delAlarm(alarm.ano)"
                ><v-icon>mdi-close-circle-outline</v-icon></v-btn
              >
            </v-col>
          </v-row>
          <b-modal
            v-model="modalShow"
            centered
            hide-footer
            title="어떤 알림이 왔을까요?"
          >
            <v-textarea
              :value="alarmLocal.asummary"
              rows="1"
              auto-grow
              disabled
            ></v-textarea>
            <div style="float:right">
              <button class="btn-message" @click="goRouting(alarmLocal)">
                이동
              </button>

              <button class="btn-message" @click="delAlarm(alarmLocal.ano)">
                삭제
              </button>
            </div>
          </b-modal>
        </v-list>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn text @click="menu = false">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-menu>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      menu: false,
      alarms: [],
      userInfo: {},
      imgPath: "",
      alarmLen: "",
      offset: true,
      categoryList: [],
      isLoadingUser: true,
      isLoadingAlarms: true,
      isloadingCategoryList: true,
      modalShow: false,
      alarmLocal: {},
    };
  },

  created() {
    this.getAlarmsList();
    setInterval(this.getAlarmsList, 3000);
    var storage = window.sessionStorage;
    var params = new URLSearchParams();
    params.append("email", storage.getItem("user-email"));

    axios
      .post("profile", params)
      .then((response) => {
        this.userInfo.uname = response.data["user-name"];
        this.userInfo.utel = response.data["user-tel"];
        this.userInfo.uprofileImg = response.data["profile-img"];
        this.userInfo.uno = response.data["user-uno"];
        this.imgPath = require(`@/assets/images/avatars/${this.userInfo.uprofileImg}.png`);
        this.isLoadingUser = false;
      })
      .catch((error) => {
        console.log(error);
      });

    axios
      .get("getCategory")
      .then((response) => {
        this.categories = response.data.categories;
        this.isloadingCategoryList = false;
      })
      .catch((error) => {
        console.log(error);
      });
  },

  computed: {
    alarmBgColor() {
      return (aisRead) => {
        return aisRead ? "#E8E1D5" : "white";
      };
    },
  },

  methods: {
    goRouting(alarm) {
      this.modalShow = false;
      var params = new URLSearchParams();
      if (alarm.aurl === "FriendInfo") {
        this.menu = false;
        this.$store.commit("setRouteUrl", "/friendProfile");
        this.$router.push({
          name: "FriendProfile",
          params: {
            myUno: this.userInfo.uno,
            friendUno: alarm.aurlNo,
            categoryList: this.categoryList,
          },
        });
      } else if (alarm.aurl === "GroupMainPage") {
        var gno = alarm.aurlNo;
        params.append("email", window.sessionStorage.getItem("user-email"));
        params.append("gno", gno);
        axios
          .post("isGroupMember", params)
          .then((response) => {
            this.menu = false;
            this.$store.commit("setGno", gno);
            this.$store.commit("setMemberStatus", response.data.memberStatus);
            this.$store.commit("setRouteUrl", "/group");
            this.$router
              .push({ name: "GroupMainPage", params: { ano: alarm.ano } })
              .catch(() => {});
          })
          .catch((error) => {
            console.log(error);
          });
      } else if (alarm.aurl === "BoardDetail") {
        var bno = alarm.aurlNo;
        params.append("bno", bno);
        params.append("email", window.sessionStorage.getItem("user-email"));
        axios.post("boardDetail", params).then((resp) => {
          if (!resp.data.isExist) {
            alert("삭제된 게시글입니다.");
            this.menu = false;
            this.$store.commit("setRouteUrl", "/");
            this.$router.push("/");
            return;
          }
          this.$store.commit("setIsWriter", resp.data.isWriter);

          params = new URLSearchParams();
          params.append("email", window.sessionStorage.getItem("user-email"));
          params.append("gno", resp.data.curBoard.bgno);

          axios.post("isGroupMember", params).then((response) => {
            this.menu = false;
            this.$store.commit("setGno", gno);
            this.$store.commit("setMemberStatus", response.data.memberStatus);
            this.$store.commit("setRouteUrl", "/board/detail");
            this.$router.push("/board/detail").catch(() => {});
          });
        });
      }
    },
    getAlarmsList() {
      var storage = window.sessionStorage;
      var params = new URLSearchParams();
      params.append("email", storage.getItem("user-email"));

      axios
        .post("getAlarms", params)
        .then((response) => {
          this.alarms = response.data.alarms;
          this.alarmLen = response.data.notReadAlarm;
          this.isLoadingAlarms = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    delAlarm(ano) {
      this.modalShow = false;
      var params = new URLSearchParams();
      params.append("ano", ano);

      axios
        .post("delAlarm", params)
        .then((response) => {
          console.log(ano);
          this.getAlarmsList();
        })
        .catch((error) => {
          alert("에러");
          console.log(ano);
        });
    },
    readAlarm(alarm) {
      var params = new URLSearchParams();
      params.append("ano", alarm.ano);

      if (!alarm.aisRead) {
        axios.post("readAlarm", params).then((resp) => {
          params = new URLSearchParams();
          params.append("email", window.sessionStorage.getItem("user-email"));
          axios
            .post("getAlarms", params)
            .then((response) => {
              this.alarms = response.data.alarms;
              this.alarmLen = response.data.notReadAlarm;
            })
            .catch((error) => {
              console.log(error);
            });
        });
      }
    },
    modalShowMethod(alarm) {
      this.readAlarm(alarm);
      this.alarmLocal = alarm;
      this.modalShow = true;
    },
  },
};
</script>

<style></style>
