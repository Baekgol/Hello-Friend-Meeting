<template>
  <v-container style="height: 600px;">
    <!-- 프로필 커버 이미지 -->
    <profile-cover />

    <!-- 쪽지 -->
    <v-btn
      style="top: 80px; right: 40px;"
      color="success"
      @click="messageModal = true"
      fab
      absolute
    >
      <v-icon>mdi-email-plus</v-icon>
    </v-btn>

    <!-- 프로필 사진 -->
    <v-btn
      :style="{ left: '50%', top: '-25px', transform: 'translateX(-50%)' }"
      fab
      disabled
    >
      <v-avatar size="70"
        ><v-img
          :src="require(`@/assets/images/avatars/${info.uprofileImg}.png`)"
        ></v-img
      ></v-avatar>
    </v-btn>

    <!-- 친구 관리 버튼 -->
    <friend-manage-btn :uno="uno" :info="info" @deleteFriend="deleteFriend" />

    <v-divider></v-divider>

    <!-- 그룹 가입 목록 -->
    <friend-group-list
      :uno="uno"
      :info="info"
      :categoryList="categoryList"
      :panelsHeight="250"
    />

    <!-- 쪽지 모달 -->
    <v-dialog v-model="messageModal" width="320" persistent>
      <v-card>
        <v-card-title>
          <span>쪽지 전송</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="messageModal = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pt-4 pb-0">
          <v-row no-gutters>
            <v-text-field
              v-model="messageTitle"
              label="제목"
              outlined
              clearable
              required
            ></v-text-field>
          </v-row>
          <v-row class="mt-0" no-gutters>
            <v-textarea
              v-model="messageContent"
              rows="7"
              label="내용"
              outlined
              no-resize
              clearable
            ></v-textarea>
          </v-row>
        </v-card-text>
        <v-card-actions class="px-6">
          <v-row class="pt-0 pb-4" justify="end" no-gutters>
            <v-btn
              color="success"
              :disabled="!sendMessageValid"
              @click="sendMessage"
              >쪽지 보내기</v-btn
            >
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
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
  props: ['uno', 'info', 'categoryList'],
  data() {
    return {
      messageModal: false,
      messageTitle: null,
      messageContent: null,
      sendMessageValid: false,
      messageTitleValid: false,
      messageContentValid: false,
    };
  },
  methods: {
    deleteFriend(friendUno) {
      this.$emit('deleteFriend', friendUno);
    },
    sendMessage() {
      axios
        .post('sendMessage', {
          myUno: this.uno,
          friendUno: this.info.uno,
          title: this.messageTitle,
          content: this.messageContent,
        })
        .then((response) => {
          if (response.data['is-success'])
            alert('쪽지를 성공적으로 전송하였습니다.');
          else alert('쪽지를 전송하는데 오류가 발생하였습니다.');

          this.messageModal = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  watch: {
    messageModal(messageModal) {
      if (!messageModal) {
        this.messageTitle = null;
        this.messageContent = null;
        this.messageTitleValid = false;
        this.messageContentValid = false;
        this.sendMessageValid = false;
      }
    },
    messageTitle(messageTitle) {
      if (messageTitle != null && messageTitle != '') {
        this.messageTitleValid = true;
        if (this.messageContentValid) this.sendMessageValid = true;
      } else {
        this.messageTitleValid = false;
        this.sendMessageValid = false;
      }
    },
    messageContent(messageContent) {
      if (messageContent != null && messageContent != '') {
        this.messageContentValid = true;
        if (this.messageTitleValid) this.sendMessageValid = true;
      } else {
        this.messageContentValid = false;
        this.sendMessageValid = false;
      }
    },
  },
};
</script>

<style></style>
