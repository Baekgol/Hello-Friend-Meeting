<template>
  <div>
    <div v-if="memberStatus == 3 || memberStatus == 4">
      <GroupNav />
    </div>
    <div v-if="memberStatus == 2">
      <InviteLetter :ano="ano" />
    </div>
  </div>
</template>

<script>
import GroupNav from '@/components/group/GroupNav.vue';
import InviteLetter from '@/components/group/GroupInviteLetter.vue';
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
      var storage = window.sessionStorage;
      var params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      params.append('gno', this.gno);
      axios
        .post('isGroupMember', params)
        .then((response) => {
          if (!response.data.isExist) {
            alert('삭제된 그룹입니다.');
            this.$router.push('/');
            return;
          }
          this.memberStatus = response.data.memberStatus;
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
    },
  },
  components: {
    GroupNav,
    InviteLetter,
  },
  created() {
    this.$store.state.isHeader = true;
    this.$store.state.isFooter = true;

    var storage = window.sessionStorage;
    var params = new URLSearchParams();
    params.append('email', storage.getItem('user-email'));
    params.append('gno', this.gno);
    axios
      .post('isGroupMember', params)
      .then((response) => {
        if (!response.data.isExist) {
          alert('삭제된 그룹입니다.');
          this.$router.push('/');
          return;
        }
        this.memberStatus = response.data.memberStatus;
        this.$store.commit('setMemberStatus', this.memberStatus);
      })
      .catch((error) => {
        console.log(error);
      });
  },
  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      uno: this.$store.getters.getUno,
    };
  },
  props: ['ano'],
};
</script>

<style></style>
