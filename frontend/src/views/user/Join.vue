<template>
  <!-- 전체 -->
  <v-container class="bg-2">
    <v-icon size="30" color="white" @click="close">mdi-window-close</v-icon>
    <v-row style="margin-top: 0px; color: white;">
      <v-col>
        <v-row
          style="margin-top: 150px; margin-left: 15px; letter-spacing: -1px;"
        >
          <h1>{{ titleName }}</h1>
        </v-row>
        <v-row
          style="margin-left: 15px; margin-right: 15px; letter-spacing: -1px;"
        >
          새로운 친구의 친구가 되러 와주셨네요. <br />
          여기서 가입을 도와드릴게요 :)
        </v-row>
      </v-col>
    </v-row>
    <v-row style="margin: 10px; margin-top: 25px;">
      <v-form ref="form" lazy-validation>
        <v-carousel
          height="200"
          hide-delimiter-background
          show-arrows-on-hover
          style="margin-left: -10px;"
        >
          <template v-slot:prev="{ on, attrs }">
            <v-icon color=" rgb(70, 70, 70)" v-bind="attrs" v-on="on"
              >mdi-arrow-left-thick</v-icon
            >
            <!-- <v-btn color="success" v-bind="attrs" v-on="on">Previous slide</v-btn> -->
          </template>
          <template v-slot:next="{ on, attrs }">
            <v-icon color=" rgb(70, 70, 70)" v-bind="attrs" v-on="on"
              >mdi-arrow-right-thick</v-icon
            >
            <!-- <v-btn color="info" v-bind="attrs" v-on="on">Next slide</v-btn> -->
          </template>
          <v-carousel-item
            reverse-transition="fade-transition"
            style="width: 380px;"
          >
            <v-row style="padding: 80px;">
              <v-text-field
                ref="name"
                v-model="name"
                :rules="nameRules"
                :error-messages="errorMessages"
                label="내 이름은..."
                placeholder="김싸피"
                counter="10"
                required
              ></v-text-field>
            </v-row>
          </v-carousel-item>

          <v-carousel-item
            reverse-transition="fade-transition"
            style=" width: 380px;"
          >
            <v-row
              justify="center"
              style="padding-left: 45px; padding-right: 45px; padding-top: 30px;"
            >
              <v-col
                ><v-text-field
                  ref="emailFront"
                  v-model="emailFront"
                  :rules="[() => !!emailFront || 'This field is required']"
                  label="내 이메일 주소는..."
                  placeholder="ID"
                  required
                ></v-text-field
              ></v-col>
              <v-col
                cols="1"
                align="center"
                align-self="center"
                style="margin: -10px; padding: 0px;"
                >@</v-col
              >
              <v-col>
                <v-autocomplete
                  ref="emailBack"
                  v-model="emailBack"
                  :rules="[() => !!emailBack || 'This field is required']"
                  :items="emailBacks"
                  label=""
                  placeholder="선택..."
                  required
                >
                </v-autocomplete>
              </v-col>
            </v-row>
            <v-row style="margin: 0;">
              <v-col
                class="text-right"
                style="margin-right: 20px; margin-top: -10px;"
              >
                <v-btn small color="success" @click="dckEmail">중복확인</v-btn>
              </v-col>
            </v-row>
          </v-carousel-item>

          <v-carousel-item
            reverse-transition="fade-transition"
            style=" width: 380px;"
          >
            <v-row
              style="padding-left: 80px; padding-right: 80px; padding-top: 20px;"
            >
              <v-text-field
                ref="password"
                type="password"
                v-model="password"
                :rules="passwordRules"
                label="쉿! 비밀번호는..."
                placeholder="●●●●●●●●"
                required
              ></v-text-field>
            </v-row>
            <v-row style="padding-left: 80px; padding-right: 80px;">
              <v-text-field
                ref="passwordConfirm"
                type="password"
                v-model="passwordConfirm"
                :rules="passwordConfirmRules"
                label="비밀번호 다시..."
                required
                placeholder="●●●●●●●●"
              ></v-text-field>
            </v-row>
          </v-carousel-item>

          <v-carousel-item
            reverse-transition="fade-transition"
            style=" width: 380px;"
          >
            <v-row
              style="margin: 0; padding-left: 50px; padding-right: 60px; margin-top: 20px;"
            >
              <v-col style="padding: 0; margin-right: 10px; ">
                <v-text-field
                  ref="tel"
                  v-model="tel"
                  :rules="[() => !!tel || 'This field is required']"
                  label="내 휴대폰 번호는..."
                  required
                  hint="-없이 숫자만 입력"
                  placeholder="01012345678"
                ></v-text-field>
              </v-col>
              <v-col cols="4" style="padding: 0;" align-self="center">
                <v-btn
                  small
                  color="success"
                  style="width: 100px;"
                  @click="authTel"
                  >인증번호 받기</v-btn
                >
              </v-col>
            </v-row>
            <v-row style="margin: 0; padding-left: 50px; padding-right: 60px; ">
              <v-col style="padding: 0;  margin-right: 10px; ">
                <v-text-field
                  v-model="myAuthNum"
                  :rules="[() => !!myAuthNum || 'This field is required']"
                  label="인증 번호"
                  required
                  placeholder="1234"
                ></v-text-field>
              </v-col>
              <v-col cols="4" style="padding: 0;" align-self="center">
                <v-btn
                  small
                  color="success"
                  style="width: 100px;"
                  @click="confirmAuth"
                  >인증 확인</v-btn
                >
              </v-col>
            </v-row>
          </v-carousel-item>

          <!-- <v-carousel-item
          v-for="(slide, i) in slides"
          :key="i"
          reverse-transition="fade-transition"
        >
          <v-sheet color="rgba(255,255,255,0.2)" height="100%">
            <v-row
              class="fill-height"
              align="center"
              justify="center"
              style="margin: 0;"
            >
              <div>{{ title }}</div>
              <div>{{ label }}}</div>
              <div></div>
            </v-row>
          </v-sheet>
        </v-carousel-item> -->
        </v-carousel>
      </v-form>
    </v-row>

    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="primary" @click="validate">완 료</v-btn>
    </v-card-actions>
  </v-container>
</template>

<script>
import axios from "axios";

let tmp;
export default {
  created() {
    // this.isAuth = false;
    // this.isDckEmail = false;
    tmp = this;
  },
  data: () => ({
    emailFront: "",
    emailBack: "",
    emailBacks: [
      "naver.com",
      "gmail.com",
      "hanmail.net",
      "ssafy.com",
      "직접 입력",
    ],
    errorMessages: "",
    titleName: "반가워요!",
    name: "",
    password: "",
    passwordConfirm: "",
    isDckEmail: false,
    tel: null,
    myAuthNum: null,
    realAuthNum: "dummy",
    isAuth: false,
    nameRules: [
      (v) => !!v || "이름은 꼭 알려주셨으면 좋겠어요!",
      (v) => (!!v && v.length <= 10) || "이름은 10자 이내로 멋지게 지어주세요!",
    ],
    passwordRules: [
      (v) => !!v || "비밀번호를 입력해주세요.",
      (v) => (v && v.length >= 8) || "너무 짧으면 위험하지 않을까요? ㅠ.ㅠ",
      (v) => /(?=.*[A-Za-z])/.test(v) || "문자도 포함해볼까요?",
      (v) => /(?=.*\d)/.test(v) || "숫자를 꼭 포함해야 해요!",
      (v) =>
        /([!@$%])/.test(v) ||
        "특수문자를 통해 더 안전한 비밀번호를 만들어요! [!@#$%]",
    ],
    passwordConfirmRules: [
      (v) => !!v || "비밀번호를 입력해주세요.",
      (v) => v == tmp.password || "비밀번호와 일치해야해요.",
    ],
    formHasErrors: false,
  }),

  computed: {
    form() {
      return {
        name: this.name,
        emailFront: this.emailFront,
        emailBack: this.emailBack,
        password: this.password,
        tel: this.tel,
      };
    },
  },

  watch: {
    name(v, w) {
      this.errorMessages = "";
      if (this.name.length == 0) {
        this.titleName = "반가워요!";
      } else {
        this.titleName = this.name + "님!";
      }

      if (this.name.length > 10) {
        this.name = w;
      }
    },
  },

  methods: {
    close() {
      this.$router.push("/");
    },
    dckEmail() {
      let email = this.emailFront + "@" + this.emailBack;
      var params = new URLSearchParams();
      params.append("email", email);

      axios
        .post("dckEmail", params)
        .then((response) => {
          if (response.data["is-success"]) {
            this.isDckEmail = true;
            alert("이 이메일은 이제 아이디가 될 거예요!");
          } else {
            alert("다른 이메일을 입력해주세요:(");
          }
        })
        .catch((error) => {
          alert("에러");
          console.log(error);
        });
    },
    authTel() {
      var params = new URLSearchParams();
      params.append("tel", this.tel);

      axios
        .post("authTel", params)
        .then((response) => {
          if (response.data["is-success"]) {
            this.realAuthNum = response.data["realAuthNum"];
            alert("인증 번호를 전송했어요!");
          } else {
            alert("이미 등록된 번호입니다.");
          }
        })
        .catch((error) => {
          alert("에러");
          console.log(error);
        });
    },
    confirmAuth() {
      if (this.realAuthNum == this.myAuthNum) {
        alert("인증 성공!");
        console.log("real: " + this.realAuthNum + " / my: " + this.myAuthNum);
        this.isAuth = true;
      } else {
        alert("인증 실패");
        console.log("real: " + this.realAuthNum + " / my: " + this.myAuthNum);
        this.isAuth = false;
      }
    },
    validate() {
      if (this.$refs.form.validate() && this.isDckEmail && this.isAuth) {
        this.submit();
      } else {
        alert("진행 안된 게 있음"); // 경우 나눠서 알려주면
      }
      // this.formHasErrors = false;

      // Object.keys(this.form).forEach((f) => {
      //   if (!this.form[f]) this.formHasErrors = true;
      //   this.$refs[f].validate(true);
      // });

      // if (this.formHasErrors) {
      //   this.submit();
      // }
    },
    submit() {
      let email = this.emailFront + "@" + this.emailBack;

      axios
        .post("join", {
          uname: this.name,
          email: email,
          password: this.password,
          tel: this.tel,
        })
        .then((response) => {
          alert(this.name + "님! 새로운 친구가 되어주셔서 감사해요:)");
          this.$router.push("/");
        })
        .catch((error) => {
          alert("가입 도중 오류가 발생하였습니다.");
        });
    },
    // submit() {
    //   this.formHasErrors = false;

    //   Object.keys(this.form).forEach((f) => {
    //     if (!this.form[f]) this.formHasErrors = true;
    //     // this.$refs.form.validate();
    //     this.$refs[f].validate(true);
    //   });
    // },
  },
};
</script>
<style>
html,
body {
  width: 100%;
  height: 100%;
}
.bg-2 {
  width: 100%;
  height: 100%;
  background: linear-gradient(
      to bottom,
      rgba(44, 1, 66, 0.486),
      rgba(0, 0, 0, 0.788)
    ),
    url("~@/assets/images/join.gif") no-repeat center center fixed;
  background-size: cover;
}
</style>
