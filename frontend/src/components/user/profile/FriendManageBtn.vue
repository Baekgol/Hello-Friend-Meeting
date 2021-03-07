<template>
  <v-row>
    <v-col v-if="friendStatus != -1">
      <v-btn
        v-if="friendStatus == 0"
        color="primary"
        @click="applyFriend"
        block
      >
        친구 요청
      </v-btn>

      <v-btn
        v-else-if="friendStatus == 1"
        color="warning"
        @click="cancelRequest"
        block
      >
        친구 요청 취소
      </v-btn>

      <v-btn
        v-else-if="friendStatus == 2"
        color="success"
        @click="acceptFriend"
        block
      >
        친구 요청 수락
      </v-btn>

      <v-btn
        v-else-if="friendStatus == 3"
        color="error"
        @click="deleteModal = true"
        block
      >
        친구 삭제
      </v-btn>
    </v-col>
    <v-col v-if="friendStatus == 2">
      <v-btn color="warning" @click="denyFriend" block>
        친구 요청 거절
      </v-btn>
    </v-col>

    <!-- 친구 삭제 모달 -->
    <v-dialog v-model="deleteModal" max-width="270px" persistent>
      <v-card>
        <v-card-title>
          <span>정말 삭제하시겠습니까?</span>
        </v-card-title>
        <v-card-actions>
          <v-row class="ma-0" justify="end">
            <v-btn
              color="error"
              class="font-weight-black"
              style="margin-right: 10px;"
              @click="deleteFriend"
            >
              확인
            </v-btn>
            <v-btn color="success" @click="deleteModal = false">
              취소
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import axios from 'axios';

export default {
  props: ['uno', 'info'],
  data() {
    return {
      friendStatus: -1,
      deleteModal: false,
    };
  },
  created() {
    axios
      .post('isFriend', { myUno: this.uno, friendUno: this.info.uno })
      .then((response) => {
        this.friendStatus = response.data;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    applyFriend() {
      axios
        .post('applyFriend', { myUno: this.uno, friendUno: this.info.uno })
        .then((response) => {
          if (response.data['is-success']) {
            this.friendStatus = 1;
            alert(this.info.uname + '님께 친구 요청을 보냈습니다.');
          } else alert('친구 요청을 하는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
    cancelRequest() {
      axios
        .post('cancelRequest', { myUno: this.uno, friendUno: this.info.uno })
        .then((response) => {
          if (response.data['is-success']) {
            this.friendStatus = 0;
            alert('친구 요청을 취소하였습니다.');
          } else alert('친구 요청을 취소하는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
    acceptFriend() {
      axios
        .post('acceptFriend', { myUno: this.uno, friendUno: this.info.uno })
        .then((response) => {
          if (response.data['is-success']) {
            this.friendStatus = 3;
            alert(this.info.uname + '님과 친구가 되었습니다.');
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    denyFriend() {
      axios
        .post('denyFriend', { myUno: this.uno, friendUno: this.info.uno })
        .then((response) => {
          if (response.data['is-success']) {
            this.friendStatus = 0;
            alert('친구 요청을 거절하였습니다.');
          } else alert('친구 요청을 거절하는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
    deleteFriend() {
      axios
        .post('deleteFriend', { myUno: this.uno, friendUno: this.info.uno })
        .then((response) => {
          if (response.data['is-success']) {
            this.$emit('deleteFriend', this.info.uno);
            this.friendStatus = 0;
            this.deleteModal = false;
            alert('친구를 삭제하였습니다.');
          } else alert('친구를 삭제하는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
