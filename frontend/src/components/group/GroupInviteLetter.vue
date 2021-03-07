<template>
  <v-container
    v-if="!isLoadingGroupInfo"
    class="Bg"
    :style="'height: ' + height + 'px'"
  >
    <v-img
      src="@/assets/images/invitation.gif"
      style="margin-top: 0; margin-left: 30px;"
      justify="center"
    ></v-img>
    <v-row
      justify="center"
      style="font-size: 3rem; font-weight: bold; margin: 15px;"
    >
      {{ gname }}
    </v-row>
    <v-row
      justify="center"
      style="font-size: 1rem; margin-left: 10px; margin-right: 10px;"
    >
      {{ gdesc }}
    </v-row>
    <v-row style="margin-top: 250px;">
      <v-col align="center" align-self="center" style="padding: 0;">
        <v-btn width="100" class="primary" @click="accept">수락</v-btn>
      </v-col>
      <v-col align="center" align-self="center" style="padding: 0;">
        <v-btn width="100" class="warning" @click="reject">거절</v-btn>
      </v-col>
    </v-row>
  </v-container>

  <v-row
    v-else
    class="ma-0"
    style="height: 663px;"
    align="center"
    justify="center"
  >
    <v-progress-circular indeterminate color="purple"></v-progress-circular>
  </v-row>
</template>

<script>
const storage = window.sessionStorage;
import axios from 'axios';

export default {
  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      gname: '',
      gdesc: '',
      height: window.screen.height - 112,
      isLoadingGroupInfo: true,
    };
  },
  props: ['ano'],
  created() {
    var params = new URLSearchParams();
    params.append('gno', this.gno);
    axios
      .post('getGroupInfo', params)
      .then((response) => {
        this.gname = response.data.groupInfo.gname;
        this.gdesc = response.data.groupInfo.gdesc;
        this.isLoadingGroupInfo = false;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    accept() {
      var params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      params.append('gno', this.gno);

      axios
        .post('acceptInviteGroup', params)
        .then((response) => {
          alert(response.data.data);
          this.$store.commit('setMemberStatus', 3);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    reject() {
      var params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      params.append('gno', this.gno);

      axios
        .post('denyInviteGroup', params)
        .then((response) => {
          alert(response.data.data);
          this.delInviteAlarm();
          this.$store.commit('setMemberStatus', 0);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    delInviteAlarm() {
      var params = new URLSearchParams();
      params.append('ano', this.ano);

      axios
        .post('delAlarm', params)
        .then((response) => {
          console.log(response);
          this.$router.push('/feed/main');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style>
.Bg {
  background: url('~@/assets/images/petals.gif'),
    url('~@/assets/images/invite.gif') center center;
  background-repeat: no-repeat;
  /* background-attachment: fixed; */
  background-size: cover;
}
</style>
