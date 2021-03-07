<template>
  <v-card style="padding:0" min-height="710px">
    <v-tabs
      class="group-bar rounded-0"
      v-model="tab"
      background-color="#212034"
      color="#E2B9C6"
      dark
      grow
    >
      <v-tab>게시판</v-tab>
      <v-tab>일정</v-tab>
      <v-tab>채팅</v-tab>
      <v-tab>그룹원</v-tab>
    </v-tabs>

    <v-tabs-items v-model="tab">
      <v-tab-item>
        <v-card flat> <GroupBoard /></v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat> <GroupSchedule /></v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card v-if="tab == 2" flat><GroupChat /> </v-card>
      </v-tab-item>
      <v-tab-item>
        <v-card flat><GroupMemberList /> </v-card>
      </v-tab-item>
    </v-tabs-items>
  </v-card>
</template>
<script>
import GroupBoard from '@/components/group/GroupBoard.vue';
import GroupSchedule from '@/components/group/GroupSchedule.vue';
import GroupChat from '@/components/group/GroupChat.vue';
import GroupMemberList from '@/components/group/GroupMemberList.vue';

export default {
  created() {
    this.height = window.screen.height - 112;
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
  },
  watch: {
    vuexGno(val) {
      this.gno = val;
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
    tab(val) {
      this.$store.commit('setTabNum', val);
    },
  },
  components: {
    GroupBoard,
    GroupSchedule,
    GroupChat,
    GroupMemberList,
  },

  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      uno: this.$store.getters.getUno,
      bno: this.$store.getters.getBno,
      tab: null,
      height: 0,
    };
  },
  methods: {
    change(ms) {
      this.memberStatus = ms;
    },
  },
};
</script>

<style>
.group-bar {
  position: -webkit-sticky; /* 사파리 브라우저 지원 */
  position: sticky;
  top: 56px;
  z-index: 2;
}
</style>
