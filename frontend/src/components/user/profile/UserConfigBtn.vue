<template>
  <div>
    <v-speed-dial
      v-model="userConfigFab"
      absolute
      top
      right
      direction="left"
      transition="slide-x-reverse-transition"
    >
      <template v-slot:activator>
        <v-btn v-model="userConfigFab" color="success" small fab dark>
          <v-icon>mdi-cog</v-icon>
        </v-btn>
      </template>

      <!-- 회원탈퇴 -->
      <v-btn fab dark small color="red" @click="deleteModal = true">
        <v-icon>mdi-account-remove</v-icon>
      </v-btn>

      <!-- 비밀번호 변경 -->
      <v-btn fab dark small color="indigo" @click="dialog = true">
        <v-icon>mdi-lock-reset</v-icon>
      </v-btn>

      <!-- 로그아웃 -->
      <v-btn fab dark small color="green" @click="logout">
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-speed-dial>

    <!--비밀번호변경 모달-->
    <v-dialog v-model="dialog" persistent>
      <v-card>
        <v-card-title>
          <span>비밀번호변경</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="changeView">
            <v-icon v-if="passwordShow">mdi-eye</v-icon>
            <v-icon v-if="!passwordShow">mdi-eye-off</v-icon>
          </v-btn>
          <v-btn icon @click="closeDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-form v-model="formCheck">
          <v-card-text>
            <v-text-field
              v-model="password"
              label="기존 비밀번호"
              hide-details="auto"
              placeholder="기존 비밀번호를 입력해주세요."
              clearable
              required
              :type="passwordShow ? 'text' : 'password'"
            ></v-text-field>

            <v-text-field
              v-model="newPassword"
              label="새 비밀번호"
              hide-details="auto"
              :rules="passwordRules"
              placeholder="새로운 비밀번호를 입력해주세요."
              clearable
              required
              :type="passwordShow ? 'text' : 'password'"
            ></v-text-field>

            <v-text-field
              v-model="passwordConfirm"
              label="비밀번호 확인"
              hide-details="auto"
              :rules="passwordConfirmRules"
              placeholder="비밀번호를 다시 입력해주세요."
              clearable
              required
              :type="passwordShow ? 'text' : 'password'"
            ></v-text-field>
          </v-card-text>
        </v-form>
        <v-card-actions style="padding-top: 0;">
          <v-row class="ma-0" justify="end">
            <v-btn
              color="primary"
              class="font-weight-black"
              :disabled="!formCheck"
              @click="modify"
            >
              변경
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 회원탈퇴 모달 -->
    <v-dialog v-model="deleteModal" persistent>
      <v-card>
        <v-card-title>
          <span>회원탈퇴</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="deleteModal = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <v-text-field
            v-model="password"
            placeholder="비밀번호를 입력해주세요."
            clearable
            required
            :type="passwordShow ? 'text' : 'password'"
            :append-icon="passwordShow ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="passwordShow = !passwordShow"
          ></v-text-field>
        </v-card-text>

        <v-card-actions style="padding-top: 0;">
          <v-row class="ma-0" justify="end">
            <v-btn
              color="warning"
              class="font-weight-black"
              :disabled="!deleteUserValid"
              @click="deleteAlertModal = !deleteAlertModal"
            >
              탈퇴하기
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 회원탈퇴 경고 모달 -->
    <v-dialog v-model="deleteAlertModal" width="270" persistent>
      <v-card>
        <v-card-title>
          <span>정말 탈퇴하시겠습니까?</span>
        </v-card-title>
        <v-card-actions>
          <v-row class="ma-0" justify="end">
            <v-btn
              color="error"
              class="font-weight-black"
              style="margin-right: 10px;"
              :disabled="!deleteUserValid"
              @click="deleteUser"
            >
              확인
            </v-btn>
            <v-btn color="success" @click="deleteAlertModal = false">
              취소
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from 'axios';
const storage = window.sessionStorage;

let tmp;

export default {
  data() {
    return {
      userConfigFab: false,
      deleteModal: false,
      deleteAlertModal: false,
      password: '',
      passwordShow: false,
      deleteUserValid: false,
      dialog: false,
      newPassword: '',
      passwordConfirm: '',
      formCheck: false,
      passwordRules: [
        (v) => !!v || '새로운 비밀번호를 입력해주세요.',
        (v) => (v && v.length >= 8) || '너무 짧으면 위험하지 않을까요? ㅠ.ㅠ',
        (v) => /(?=.*[A-Za-z])/.test(v) || '문자도 포함해볼까요?',
        (v) => /(?=.*\d)/.test(v) || '숫자를 꼭 포함해야 해요!',
        (v) =>
          /([!@$%])/.test(v) ||
          '특수문자를 통해 더 안전한 비밀번호를 만들어요! [!@#$%]',
      ],
      passwordConfirmRules: [
        (v) => !!v || '새로운 비밀번호를 입력해주세요.',
        (v) => v == tmp.newPassword || '비밀번호와 일치해야해요.',
      ],
    };
  },
  created() {
    tmp = this;
  },
  methods: {
    logout() {
      this.$store.commit('resetFeedCreatedTime');
      this.$store.commit('setRouteUrl', '/');
      storage.removeItem('auth-token');
      storage.removeItem('user-email');
      this.$router.push('/');
    },
    deleteUser() {
      let params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      params.append('password', this.password);

      axios
        .post('delete', params)
        .then((response) => {
          if (response.data['is-success']) {
            storage.removeItem('auth-token');
            storage.removeItem('user-email');
            alert('회원탈퇴가 완료되었습니다. 로그인 페이지로 이동합니다.');
            this.$router.push('/');
          } else {
            alert('잘못된 비밀번호를 입력하였습니다.');
            this.deleteAlertModal = false;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    closeDialog() {
      this.password = '';
      this.newPassword = '';
      this.passwordConfirm = '';
      this.dialog = false;
      this.passwordShow = false;
    },
    changeView() {
      this.passwordShow = !this.passwordShow;
    },
    modify() {
      let params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));
      params.append('oldPassword', this.password);
      params.append('newPassword', this.newPassword);

      axios
        .create({
          headers: {
            'auth-token': storage.getItem('auth-token'),
          },
        })
        .post('modify', params)
        .then(
          (response) => {
            if (response.data['is-success']) {
              this.isSubmit = true;
              storage.removeItem('auth-token');
              storage.removeItem('user-email');
              alert('비밀번호 변경이 완료되었습니다. 다시 로그인 해주세요.');
              this.$router.push('/');
            } else {
              alert('비밀번호가 틀린것같아요 ㅠ.ㅠ');
              this.closeDialog();
            }
          },
          (error) => {
            this.isSubmit = true;
            alert('비밀번호 변경 도중 오류가 발생하였습니다.');
            this.$router.push('/error');
          }
        );
    },
  },
  watch: {
    deleteModal(deleteModal) {
      if (!deleteModal) this.password = '';
    },
    password(password) {
      if (password == null || password.length == 0)
        this.deleteUserValid = false;
      else this.deleteUserValid = true;
    },
  },
};
</script>

<style></style>
