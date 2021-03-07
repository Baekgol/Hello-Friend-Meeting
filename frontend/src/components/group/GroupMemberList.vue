<template>
  <div>
    <v-container>
      <v-row no-gutters>
        <h5 style="margin:10px;">가입신청자</h5>
      </v-row>
      <ul style="margin:0; padding:0;" v-if="memberStatus === 4">
        <ol
          style="height:70px; position:relative; margin:0;"
          v-for="applier in applierList"
          :key="applier.uno"
        >
          <div>
            <v-avatar
              style="position:absolute;
                top:50%; left:5%;
                transform: translate(-50%, -50%);
                width:50px;"
              ><img
                :src="
                  require(`@/assets/images/avatars/${applier.uprofileImg}.png`)
                "
            /></v-avatar>

            <h6
              style="position:absolute;
          top:50%; left:20%;
          transform: translate(0%, -50%);"
            >
              {{ applier.uname }}
            </h6>

            <div
              style="position:absolute; top:50%; right:3%;
                transform: translate(0%, -50%); "
            >
              <v-btn @click="acceptApplier(applier.uno)">수락</v-btn>
              <v-btn @click="denyApplier(applier.uno)">거절</v-btn>
            </div>
          </div>
        </ol>
      </ul>

      <v-row no-gutters>
        <h5 style="margin:10px;">그룹 멤버</h5>
      </v-row>
      <v-row no-gutters justify="end" style="margin: 0; margin-top: -25px;">
        <v-col cols="3" align="center" style="margin: 5px;">
          <v-btn icon @click="inviteFriend = true">
            <v-icon>mdi-account-plus-outline</v-icon>
            <span
              style="margin-left: 5px; letter-spacing: -1px; font-size: 1.2rem; font-weight: bold; color: #804F69"
            >
              초대
            </span>
          </v-btn>
        </v-col>

        <v-dialog v-model="inviteFriend" persistent max-width="300">
          <v-card>
            <v-card-title>
              <span style="letter-spacing: -1px;">초대장 보내기 :)</span>
              <v-spacer></v-spacer>
              <v-btn icon @click="inviteFriend = false">
                <v-icon>mdi-close</v-icon>
              </v-btn>
            </v-card-title>
            <v-card-text>
              <v-list :dense="true">
                <v-list-item-content style="padding: 0;">
                  <v-list-item-title
                    justify="center"
                    style="padding: 0; font-size: 1.1rem; margin: 0;"
                  >
                    <v-icon large>mdi-human-female-dance</v-icon>
                    <span style="margin-left: 5px; letter-spacing: -2px;">
                      내 친구 목록
                    </span>
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item v-for="(friend, i) in friendList" :key="i">
                  <v-row no-gutters>
                    <v-list-item-avatar>
                      <v-img
                        style="width:50px;"
                        :src="
                          require(`@/assets/images/avatars/${friendList[i].uprofileImg}.png`)
                        "
                      ></v-img>
                    </v-list-item-avatar>
                    <v-list-item-content>
                      <v-list-item-title
                        v-text="friend.uname"
                      ></v-list-item-title>
                    </v-list-item-content>
                    <v-spacer></v-spacer>
                    <v-btn icon @click="getInvite(friend.uno)">
                      초대
                    </v-btn>
                  </v-row>
                </v-list-item>
              </v-list>
            </v-card-text>
          </v-card>
        </v-dialog>
      </v-row>
      <ul
        style="margin:0; padding:0;"
        v-if="memberStatus === 3 || memberStatus === 4"
      >
        <ol
          style="margin: 0; padding:0; "
          v-for="member in sortedMemberList"
          :key="member.uno"
        >
          <v-dialog width="350">
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                style="justify-content:start; color:black; height:60px; width:100%;"
                color="white"
                dark
                v-bind="attrs"
                v-on="on"
              >
                <v-avatar style="width:50px">
                  <img
                    :src="
                      require(`@/assets/images/avatars/${member.uprofileImg}.png`)
                    "
                  />
                </v-avatar>

                <h6
                  class="text-truncate"
                  style="margin:0 3%; max-width: 150px;;
                 "
                >
                  {{ member.uname }}
                </h6>
                <v-icon
                  style="color:yellow; display:inline-block; "
                  v-if="member.uno === unoOfGmaster"
                  >mdi-crown</v-icon
                >

                <v-avatar
                  style="position:absolute; right:3%;"
                  v-if="email == member.email"
                  color="orange"
                  size="48"
                >
                  <span
                    style="position:absolute;
                    top:50%; left:50%;
                    transform: translate(-55%, -50%);"
                    class="white--text headline"
                    >나</span
                  >
                </v-avatar>
              </v-btn>
            </template>
            <v-card>
              <v-card-title class="headline" style="background-color:#ebccf3">
                {{ member.uname }}
              </v-card-title>
              <div style="height:100px; ">
                <h6><v-icon>mdi-email</v-icon>{{ member.email }}</h6>
                <h6><v-icon>mdi-cellphone</v-icon>{{ member.tel }}</h6>
                <div style="display:flex; justify-content: space-around">
                  <v-btn
                    @click="banishMember(member.uno)"
                    v-if="memberStatus === 4 && email != member.email"
                  >
                    추방
                  </v-btn>
                  <v-btn
                    @click="changeGmaster(member.uno)"
                    v-if="memberStatus === 4 && email != member.email"
                  >
                    새 그룹장으로 임명
                  </v-btn>
                  <v-btn v-if="email === member.email" @click="getoutGroup()">
                    탈퇴
                  </v-btn>
                </div>
              </div></v-card
            >
          </v-dialog>
        </ol>
      </ul>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';

export default {
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
  },
  watch: {
    vuexGno(val) {
      this.gno = val;
      this.getFriends();
      var params = new URLSearchParams();
      params.append('email', this.email);
      params.append('gno', this.gno);
      axios
        .post('isGroupMember', params)
        .then((response) => {
          var memberStatus = response.data.memberStatus;
          var gno = this.gno;
          this.$store.commit('setGno', gno);
          this.$store.commit('setMemberStatus', memberStatus);
          console.log(this.memberStatus);
          this.getUsers();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    vuexUno(val) {
      this.uno = val;
    },
    vuexBno(val) {
      this.bno = val;
    },
    vuexMemberStatus(val) {
      this.memberStatus = val;
      this.getUsers();
    },
  },
  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      uno: this.$store.getters.getUno,
      memberList: [],
      sortedMemberList: [],
      applierList: [],
      email: window.sessionStorage.getItem('user-email'),
      unoOfGmaster: 0,
      inviteFriend: false,
      friendList: [],
    };
  },
  created() {
    this.getFriends();
    var params = new URLSearchParams();
    params.append('email', this.email);
    params.append('gno', this.gno);
    axios
      .post('isGroupMember', params)
      .then((response) => {
        var memberStatus = response.data.memberStatus;
        var gno = this.gno;
        this.$store.commit('setGno', gno);
        this.$store.commit('setMemberStatus', memberStatus);
        console.log(this.memberStatus);
        this.getUsers();
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    getUsers() {
      var params = new URLSearchParams();
      params.append('gno', this.gno);
      console.log(this.gno);
      console.log(this.gno);
      console.log(this.gno);
      axios.post('unoOfGmaster', params).then((resp) => {
        this.unoOfGmaster = resp.data.gmasterUno;
        console.log('그룹장 유저넘버', this.unoOfGmaster);
        axios
          .post('getUserListInGroup', params)
          .then((response) => {
            this.memberList = response.data.userList;
            console.log('정렬하기전멤버리스트', this.memberList);
            this.sortedMemberList = [];

            for (let i = 0; i < this.memberList.length; i++) {
              if (this.memberList[i].uno === this.unoOfGmaster) {
                this.sortedMemberList.push(this.memberList[i]);
                console.log('첫번째포문', this.memberList[i]);
              }
            }
            for (let i = 0; i < this.memberList.length; i++) {
              if (
                this.memberList[i].email === this.email &&
                this.memberList[i].uno != this.unoOfGmaster
              ) {
                this.sortedMemberList.push(this.memberList[i]);
              }
            }
            for (let i = 0; i < this.memberList.length; i++) {
              if (
                this.memberList[i].uno != this.unoOfGmaster &&
                this.memberList[i].email != this.email
              ) {
                this.sortedMemberList.push(this.memberList[i]);
              }
            }
            console.log('정렬된 멤버리스트', this.sortedMemberList);
          })
          .catch((error) => {
            console.log(error);
          });
      });
      axios
        .post('getGroupApplier', params)
        .then((response) => {
          this.applierList = response.data.applierList;
          console.log(this.applierList);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    acceptApplier(uno) {
      var params = new URLSearchParams();
      params.append('uno', uno);
      params.append('gno', this.gno);
      axios
        .post('acceptApplyGroup', params)
        .then((response) => {
          alert(response.data.data);
          this.getUsers();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    denyApplier(uno) {
      var params = new URLSearchParams();
      params.append('uno', uno);
      params.append('gno', this.gno);
      axios
        .post('denyApplyGroup', params)
        .then((response) => {
          alert(response.data.data);
          this.getUsers();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    banishMember(uno) {
      var params = new URLSearchParams();
      params.append('uno', uno);
      params.append('gno', this.gno);
      axios
        .post('banishMember', params)
        .then((response) => {
          alert(response.data.data);
          this.getUsers();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getoutGroup() {
      if (this.memberStatus === 4) {
        alert('다른 사람을 그룹장으로 임명하고 다시 시도해주세요.');
      } else {
        var params = new URLSearchParams();
        params.append('email', this.email);
        params.append('gno', this.gno);
        axios
          .post('getoutGroup', params)
          .then((response) => {
            alert(response.data.data);
            this.$router.push('/');
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    changeGmaster(uno) {
      var params = new URLSearchParams();
      params.append('nextMaster', uno);
      params.append('gno', this.gno);
      axios
        .post('changeGroupMaster', params)
        .then((response) => {
          this.unoOfGmaster = uno;
          alert(response.data.data);
          params = new URLSearchParams();
          params.append('email', this.email);
          params.append('gno', this.gno);
          axios
            .post('isGroupMember', params)
            .then((response) => {
              var memberStatus = response.data.memberStatus;
              var gno = this.gno;
              this.$store.commit('setGno', gno);
              this.$store.commit('setMemberStatus', memberStatus);
            })
            .catch((error) => {
              console.log(error);
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getFriends() {
      axios
        .post('getFriendList', {
          email: this.email,
        })
        .then((response) => {
          // console.log('●●●●●●●●●●●●●●●●●', response);
          this.friendList = response.data.friendList;

          for (let i = 0; i < this.friendList.length; i++) {
            this.friendList[i].profileUrl =
              '@/assets/images/avatars/' +
              response.data.friendList[i].uprofileImg +
              '.png';
            // console.log('●●●●●●●●●●●●●●●●●', this.friendList[i].profileUrl);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getInvite(thisFriendId) {
      var params = new URLSearchParams();
      params.append('email', this.email);
      params.append('friendId', thisFriendId);
      params.append('gno', this.gno);

      axios
        .post('inviteGroup', params)
        .then((response) => {
          if (response.data.result) alert('초대장을 발송했습니다. 두근두근...');
          else alert(response.data.msg);
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
