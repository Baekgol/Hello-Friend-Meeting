<template>
  <v-card v-if="myFeeds != null" flat tile style="background-color: white;">
    <v-row style="margin: 10px;">
      <div style="font-size: 1.8rem; letter-spacing: -2px; color: #804F69;">
        안녕하세요, <strong>{{ myName }}</strong
        >님!
      </div>
      <div class="text-center">
        <v-snackbar
          v-model="snackbar"
          :timeout="timeout"
          rounded="pill"
          color="#804F69"
          ><div style="text-align:center; color: #E8E1D5;">
            안녕하세요, <strong>{{ myName }}</strong
            >님!
          </div>
        </v-snackbar>
      </div>

      <div style="letter-spacing: -1px;">
        친구들이 만든 그룹과 새로 등록된 일정을 볼 수 있어요.
      </div></v-row
    >

    <v-container v-if="myFeeds.length != 0">
      <v-row>
        <v-spacer></v-spacer>
        <v-col
          v-for="(feed, index) in myFeeds"
          :key="feed.ano"
          cols="12"
          sm="6"
          md="4"
        >
          <v-card>
            <v-img
              :src="
                require(`@/assets/images/feeds/${getImage(
                  feed.aurl,
                  feed.asummary
                )}.gif`)
              "
              gradient="to top, rgba(20,20,20,.4), rgba(0,0,0,.7)"
              height="140px"
            >
              <v-row>
                <v-col
                  cols="10"
                  style="height: 150px;"
                  @click="goRouting(feed.aurl, feed.aurlNo)"
                >
                  <span
                    class="white--text pl-4 pt-4 d-inline-block"
                    style="letter-spacing: -1px; padding: 0; margin-left: 10px; margin-top: 10px; margin-right: 30px;"
                    v-text="feed.asummary"
                  ></span>
                </v-col>
                <v-col style="padding: 0;">
                  <v-row
                    style="height: 50px; margin-top: 10px;"
                    justify="center"
                    @click="delFeed(feed.ano, index)"
                    no-gutters
                  >
                    <v-btn large icon color="#E8E1D5">
                      <v-icon>mdi-close</v-icon>
                    </v-btn>
                  </v-row>
                </v-col>
              </v-row>
            </v-img>

            <v-card-actions
              class="justify-left"
              style="background-color: #212034"
            >
              <v-row>
                <v-col
                  cols="7"
                  style="margin-left: 3px; color: #E8E1D5; font-weight: bold; font-size: 1.1rem;"
                >
                  {{ feed.createUserName }}
                </v-col>
                <v-col
                  align-self="center"
                  style="color: #E8E1D5; font-weight: light; font-style: italic; font-size: 0.7rem;"
                  v-text="parsingDate(feed.adate)"
                >
                </v-col>
              </v-row>
              <img
                height="80"
                style="position: absolute; right: 15px; bottom: 15px;  border-radius: 70%;"
                :src="require(`@/assets/images/avatars/${feed.profileImg}.png`)"
              />
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>

    <for-null v-else :myHeight="'620px'" />
  </v-card>

  <v-row
    v-else
    class="ma-0"
    style="height: 711px;"
    align="center"
    justify="center"
  >
    <v-progress-circular indeterminate color="purple"></v-progress-circular>
  </v-row>
</template>

<script>
import axios from 'axios';
import ForNull from '@/components/user/profile/ForNull.vue';

const storage = window.sessionStorage;

export default {
  components: {
    ForNull,
  },
  methods: {
    getImage(url, summary) {
      if (url == 'BoardDetail') {
        return 1;
      } else {
        if (summary.charAt(0) == '친') {
          return 2;
        } else if (summary.charAt(0) == '내') {
          return 3;
        } else if (summary.charAt(0) == '와') {
          return 4;
        } else if (summary.charAt(0) == '반') {
          return 5;
        } else if (summary.charAt(0) == '새') {
          return 6;
        } else {
          return 7;
        }
      }
    },
    goRouting(aurl, myParam) {
      var params = new URLSearchParams();
      if (aurl === 'FriendInfo') {
        this.$store.commit('setUno', myParam);
        this.$router.push('/user/friend-info').catch(() => {});
      } else if (aurl === 'GroupMainPage') {
        var gno = myParam;
        params.append('email', window.sessionStorage.getItem('user-email'));
        params.append('gno', gno);
        axios
          .post('isGroupMember', params)
          .then((response) => {
            this.$store.commit('setGno', gno);
            this.$store.commit('setMemberStatus', response.data.memberStatus);
            this.$router.push('/group').catch(() => {});
          })
          .catch((error) => {
            console.log(error);
          });
      } else if (aurl === 'BoardDetail') {
        var bno = myParam;
        params.append('bno', bno);
        params.append('email', window.sessionStorage.getItem('user-email'));
        axios.post('boardDetail', params).then((resp) => {
          this.$store.commit('setIsWriter', resp.data.isWriter);
          this.$store.commit('setGno', resp.data.curBoard.bgno);
          this.$store.commit('setBno', bno);
          params = new URLSearchParams();
          params.append('email', window.sessionStorage.getItem('user-email'));
          params.append('gno', resp.data.curBoard.bgno);

          axios.post('isGroupMember', params).then((response) => {
            this.$store.commit('setMemberStatus', response.data.memberStatus);
            this.$router.push('/board/detail').catch(() => {});
          });
        });
      }
    },
    getFeeds() {
      var params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      axios
        .post('getFeeds', params)
        .then((response) => {
          this.myFeeds = response.data.feeds;
        })
        .catch((error) => {
          console.log(error);
          console.log('error occur');
        });
    },
    delFeed(ano, index) {
      var params = new URLSearchParams();
      params.append('ano', ano);
      axios.post('delFeed', params).then((resp) => {
        this.myFeeds.splice(index, 1);
      });
    },
    parsingDate(beforeDate) {
      let afterDate = '';
      let month = '';
      let day = '';

      afterDate = beforeDate.substring(2, 4) + '. ';

      if (beforeDate.charAt(5) == '0') {
        month = beforeDate.charAt(6);
      } else {
        month = beforeDate.substring(5, 7);
      }

      if (beforeDate.charAt(8) == '0') {
        day = beforeDate.charAt(9);
      } else {
        day = beforeDate.substring(8, 10);
      }

      afterDate = afterDate + month + '. ' + day;

      return afterDate;
    },
  },
  data() {
    return {
      myFeeds: null,
      snackbar: false,
      timeout: 5000,
      myName: '',
    };
  },
  created() {
    this.$store.commit('setIsHeader', true);
    this.$store.commit('setIsFooter', true);

    if (this.$store.getters.getFeedCreatedTime === 0) this.snackbar = true;
    this.$store.commit('setFeedCreatedTime');

    var params = new URLSearchParams();
    params.append('email', storage.getItem('user-email'));

    axios.post('profile', params).then((resp) => {
      this.myName = resp.data['user-name'];
    });

    this.getFeeds();

    this.$store.commit('setIsHeader', true);
    this.$store.commit('setIsFooter', true);
  },
};
</script>

<style>
.feedBg {
  width: 100%;
  height: 100%;
  background: linear-gradient(
      to bottom,
      rgba(44, 1, 66, 0.486),
      rgba(0, 0, 0, 0.788)
    ),
    url('~@/assets/images/join.gif') no-repeat center center fixed;
  background-size: cover;
}
</style>
