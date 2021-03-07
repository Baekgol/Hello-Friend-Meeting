<template>
  <v-container class="bg">
    <v-row style="margin-top: 200px;">
      <v-col align="center">
        <img src="@/assets/images/logo.png" />
      </v-col>
    </v-row>
    <v-row>
      <v-col align="center">
        <p class="font-weight-thin" style="color: white; font-size: 1rem;">
          로그인 하러 가기
        </p>
      </v-col>
    </v-row>
    <v-row style="margin-top: -35px;">
      <v-col align="center">
        <v-btn
          icon
          color="white"
          @click="$vuetify.goTo('#startLogin', options)"
        >
          <v-icon size="50">mdi-menu-down</v-icon>
        </v-btn>
      </v-col>
    </v-row>
    <v-row style="margin-top: 600px;"></v-row>
    <v-row id="startLogin" style="margin-top: 30px;">
      <v-col
        style="
        padding-left: 20px;
        padding-right: 20px;
        background-color: rgba(255,255,255,.6);
        margin-left: 30px;
        margin-right: 30px;
        border-radius: 10px; "
      >
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-text-field
            v-model="email"
            label="이메일"
            hint="ssafy@ssafy.com"
            outlined
            rounded
            clearable
            required
            :rules="emailRules"
          ></v-text-field>

          <v-text-field
            v-model="password"
            label="비밀번호"
            outlined
            rounded
            clearable
            required
            :type="passwordShow ? 'text' : 'password'"
            :append-icon="passwordShow ? 'mdi-eye' : 'mdi-eye-off'"
            @click:append="passwordShow = !passwordShow"
            @keyup.enter="validate"
          ></v-text-field>
          <v-btn
            :disabled="!valid"
            color="success"
            class="font-weight-black"
            @click="validate"
            block
          >
            로그인
          </v-btn>
        </v-form>
      </v-col>
    </v-row>
    <v-divider></v-divider>
    <v-row style="padding-left: 40px; padding-right: 40px;">
      <v-col style="padding-left: 0; padding-right: 0;" cols="8">
        <p class="font-weight-thin" style="color: white;">
          혹시 아직 회원이 아니신가요?
        </p>
      </v-col>
      <v-col style="padding-left: 0; padding-right: 0;" align="end">
        <router-link to="/user/join" class="font-weight-bold"
          >가입하기</router-link
        >
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';

const storage = window.sessionStorage;

export default {
  data() {
    return {
      valid: false,
      email: '',
      password: '',
      emailRules: [
        (v) =>
          /.+@.+\..+/.test(v) ||
          v == null ||
          v.length == 0 ||
          '올바른 이메일 형식을 입력해주세요.',
      ],
      passwordShow: false,
      duration: 1000,
    };
  },
  created() {
    if (storage.getItem('auth-token')) {
      this.$router.push('/feed/main');
    }

    this.$store.commit('setIsHeader', false);
    this.$store.commit('setIsFooter', false);
  },
  computed: {
    options() {
      return {
        duration: this.duration,
      };
    },
  },
  methods: {
    validate() {
      if (this.$refs.form.validate()) this.login();
    },
    login() {
      var params = new URLSearchParams();
      params.append('email', this.email);
      params.append('password', this.password);

      axios
        .post('login', params)
        .then((response) => {
          if (response.data['is-success']) {
            storage.setItem('auth-token', response.data['auth-token']);
            storage.setItem('user-email', response.data['user-email']);
            this.$router.push('/feed/main');
          } else {
            alert('아이디 또는 비밀번호를 잘못 입력하였습니다.');
          }
        })
        .catch((error) => {
          alert('로그인 도중 오류가 발생하였습니다.');
        });
    },
  },
  watch: {
    valid(valid) {
      if (valid) {
        if (this.email == null || this.email.length == 0) this.valid = false;
        else if (this.password == null || this.password.length == 0)
          this.valid = false;
      }
    },
    email(email) {
      if (email == null || email.length == 0) this.valid = false;
    },
    password(password) {
      if (this.valid && (password == null || password.length == 0))
        this.valid = false;
      else if (!this.valid && password.length > 0) this.valid = true;
    },
  },
};
</script>

<style>
.bg {
  background: linear-gradient(
      to bottom,
      rgba(47, 6, 122, 0.205),
      rgba(0, 0, 0, 0.5)
    ),
    url('~@/assets/images/main.gif') no-repeat right top fixed;
  background-size: cover;
}
</style>
