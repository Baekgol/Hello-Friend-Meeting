<template>
  <v-container v-if="friend != null" style="height: 711px;">
    <!-- 프로필 커버 이미지 -->
    <profile-cover />

    <!-- 프로필 사진 -->
    <v-btn
      :style="{ left: '50%', top: '160px', transform: 'translateX(-50%)' }"
      fab
      absolute
      disabled
    >
      <v-avatar size="80"
        ><v-img
          :src="require(`@/assets/images/avatars/${friend.uprofileImg}.png`)"
        ></v-img
      ></v-avatar>
    </v-btn>

    <!-- 친구 관리 버튼 -->
    <div style="margin-top: 45px;">
      <friend-manage-btn :uno="myUno" :info="friend" />
    </div>

    <v-divider></v-divider>

    <!-- 그룹 가입 목록 -->
    <friend-group-list
      :uno="myUno"
      :info="friend"
      :categoryList="categoryList"
      :panelsHeight="300"
    />
  </v-container>
</template>

<script>
import ProfileCover from '@/components/user/profile/ProfileCover.vue';
import FriendManageBtn from '@/components/user/profile/FriendManageBtn.vue';
import FriendGroupList from '@/components/user/profile/FriendGroupList.vue';
import axios from 'axios';

const storage = window.sessionStorage;

export default {
  components: {
    ProfileCover,
    FriendManageBtn,
    FriendGroupList,
  },
  props: ['myUno', 'friendUno', 'categoryList'],
  data() {
    return {
      friend: null,
    };
  },
  computed: {
    vuexUno() {
      return this.$store.getters.getUno;
    },
  },
  watch: {
    vuexUno() {
      axios
        .post('findUserByUno', { uno: this.friendUno })
        .then((response) => {
          if (response.data['is-success']) this.friend = response.data.user;
          else alert('친구 정보를 불러오는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  created() {
    axios
      .post('findUserByUno', { uno: this.friendUno })
      .then((response) => {
        if (response.data['is-success']) this.friend = response.data.user;
        else alert('친구 정보를 불러오는데 오류가 발생하였습니다.');
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {},
};
</script>

<style></style>
