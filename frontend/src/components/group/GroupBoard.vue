<template>
  <v-container
    v-if="!isLoadingGroupInfo && !isLoadingBoardList"
    id="scroll-target"
    class="pa-0 overflow-y-auto rounded-0"
    style="height: 663px;"
    v-scroll:#scroll-target="onScroll"
  >
    <v-btn
      icon
      style="color: white; position: absolute; top: 20px; right: 20px;"
      v-if="memberStatus == 4 && nowScroll < 100"
      @click="dialog = true"
    >
      <v-icon large color="#E8E1D5">mdi-camera</v-icon>
    </v-btn>

    <v-dialog v-model="dialog" persistent>
      <v-card>
        <v-card-title>
          <span>그룹 프로필 변경</span>
          <v-spacer></v-spacer>

          <v-btn icon @click="closeDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-file-input
          v-model="files"
          accept="image/*"
          label="이미지를 선택해주세요!"
        ></v-file-input>
        <v-card-actions style="padding-top: 0;">
          <v-row class="ma-0" justify="end">
            <v-btn
              color="primary"
              class="font-weight-black"
              :disabled="files == null || files.length === 0"
              @click="upload"
            >
              변경
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <v-row
      class="atchBg "
      :style="{ height: scrollHeight }"
      no-gutters
      style="margin-top: 0;"
    >
      <v-col align="center" align-self="center" style="padding: 0;">
        <p
          style="color: white; letter-spacing: 2px; margin-bottom: 0; font-size: 3rem; font-weight: bold;"
          :style="{ marginTop: scrollMargin }"
        >
          {{ groupTitle }}
        </p>
        <p
          style="color: white; letter-spacing: -1px; margin-left: 20px; margin-right: 20px; margin-bottom: 0; font-size: 1rem; font-weight: light;"
        >
          {{ groupDesc }}
        </p>
      </v-col>
    </v-row>

    <!-- 공지사항 -->
    <v-row style="margin: 0px; margin-top: 0;" v-if="tableNotice.length != 0">
      <v-col class="pa-0">
        <v-list style="padding: 0;">
          <v-list-group no-action v-model="active" active-class="noticeBar">
            <template v-slot:activator>
              <v-list-item-content>
                <v-list-item-title
                  style="font-size: 1.5rem; margin: 0; margin-left: 3px;"
                  ><v-icon>mdi-clipboard-check-multiple-outline</v-icon>
                  <span style="margin-left: 5px; letter-spacing: -2px;"
                    >Notice</span
                  ></v-list-item-title
                >
              </v-list-item-content>
            </template>
            <v-list-item
              v-for="item in tableNotice"
              :key="item.bno"
              @click="boardDetail(item.bno)"
              style="padding-left: 30px; padding-right: 20px;  height: 40px;"
            >
              <v-list-item-content style="padding: 0">
                <v-list-item-title>
                  <v-row no-gutters>
                    <v-col cols="1">
                      <v-icon>mdi-check-underline</v-icon>
                    </v-col>
                    <v-col
                      cols="8"
                      align-self="center"
                      style="font-size: 1rem; margin: 0; padding: 0; letter-spacing: -1px; color: red;"
                      >{{ item.btitle }}</v-col
                    >
                    <v-col
                      align="right"
                      align-self="center"
                      style="font-size: 0.7rem; color: gray; margin: 0; letter-spacing: -1px; padding-right: 20px;"
                      >{{ item.parsedDate }}</v-col
                    >
                  </v-row>
                </v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-group>
        </v-list>
      </v-col>
    </v-row>

    <!-- 게시글 목록 -->
    <v-row style="margin: 0px;">
      <v-col>
        <v-row style="font-size: 1.5rem; margin: 0; padding-left: 4px; ">
          <v-icon>mdi-clipboard-multiple-outline</v-icon>
          <span style="margin-left: 5px; letter-spacing: -2px;">Board</span>
        </v-row>
        <v-row no-gutters justify="end" style="margin-top: -15px;">
          <v-col cols="3" align="center">
            <v-btn icon @click="writeModalMethod">
              <span
                style="margin-left: 5px; margin-top: -10px; letter-spacing: -1px; font-size: 1.2rem; font-weight: bold; color: #804F69"
                >글 작성</span
              >
            </v-btn>
          </v-col>
          <v-dialog v-model="writeModal" persistent max-width="300">
            <v-card>
              <v-card-title>
                <span style="letter-spacing: -1px;">새로운 글 쓰기</span>
                <v-spacer></v-spacer>
                <v-btn icon @click="writeModal = false">
                  <v-icon>mdi-close</v-icon>
                </v-btn>
              </v-card-title>
              <v-card-text>
                <v-text-field
                  v-model="newTitle"
                  label="글 제목"
                  outlined
                  clearable
                  required
                ></v-text-field>
                <v-textarea
                  rows="8"
                  style="margin-bottom:10px"
                  v-model="newContent"
                  outlined
                  required
                  label="글 내용"
                >
                </v-textarea>
                <v-row>
                  <input
                    type="checkbox"
                    v-model="newIsNotice"
                    :disabled="memberStatus != 4"
                  />공지사항으로 설정
                </v-row>
              </v-card-text>
              <v-card-actions style="padding-top: 0;">
                <v-row class="ma-0" justify="end">
                  <v-btn
                    color="primary"
                    class="font-weight-black"
                    @click="createArticle"
                  >
                    글 등록
                  </v-btn>
                </v-row>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-row>
        <v-row style="margin: 0; margin-top: 10px;" v-if="table.length == 0">
          <v-col>
            <v-row
              justify="center"
              style="font-size: 1.2rem; letter-spacing: -1px; font-weight: bold;"
            >
              이런, 아무 글이 없어요 :(
            </v-row>
            <v-row
              justify="center"
              style="font-size: 0.8rem; letter-spacing: -1px;"
            >
              첫 글의 주인공이 되어보세요!
            </v-row>
          </v-col>
        </v-row>
        <v-row style="margin: 0;" v-else>
          <v-col style="padding: 0;">
            <v-simple-table>
              <template v-slot:default>
                <tbody>
                  <tr
                    @click="boardDetail(item.bno)"
                    style="min-height: 55px;"
                    v-for="item in table"
                    :key="item.bno"
                  >
                    <td class="text-left">
                      <p
                        style="font-size: 1rem; margin: 0; letter-spacing: -1px;"
                      >
                        {{ item.btitle }}
                      </p>
                    </td>
                    <td class="text-right" width="120">
                      <p
                        style="font-size: 0.9rem; margin: 0; letter-spacing: -1px; color: #778899; font-weight: bold;"
                      >
                        {{ item.writerName }}
                      </p>

                      <p
                        style="font-size: 0.7rem; color: #B0C4DE; margin: 0; letter-spacing: -1px;"
                      >
                        {{ item.parsedDate }}
                      </p>
                    </td>
                  </tr>
                </tbody>
              </template>
            </v-simple-table>
          </v-col>
        </v-row>
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
import axios from 'axios';
export default {
  computed: {
    scrollHeight() {
      let tmp = 663 - this.nowScroll;
      return tmp + 'px';
    },
    scrollMargin() {
      let tmp = this.nowScroll;
      return tmp + 'px';
    },
    vuexGno() {
      return this.$store.getters.getGno;
    },
    vuexMemberStatus() {
      return this.$store.getters.getMemberStatus;
    },
  },
  watch: {
    vuexGno(val) {
      this.gno = val;
      this.getGroupInfo();
      this.getBoardList();
    },
    vuexMemberStatus(val) {
      this.memberStatus = val;
    },
  },
  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      groupTitle: '',
      groupDesc: '',
      table: [],
      tableNotice: [],
      newTitle: '',
      newContent: '',
      newIsNotice: false,
      circle: true,
      disabled: false,
      writeModal: false,
      active: true,
      nowScroll: 0,
      dialog: false,
      files: [],
      isLoadingGroupInfo: true,
      isLoadingBoardList: true,
    };
  },
  created() {
    this.getGroupInfo();
    this.getBoardList();
  },
  methods: {
    onScroll(e) {
      this.nowScroll = e.target.scrollTop;
    },
    boardDetail(bno) {
      this.$store.commit('setBno', bno);
      this.$router.push('/board/detail');
    },
    createArticle() {
      var params = new URLSearchParams();
      if (this.newTitle === '') {
        alert('제목을 입력해주세요!');
        return;
      } else if (this.newContent === '') {
        alert('내용을 입력해주세요!');
        return;
      }
      params.append('email', window.sessionStorage.getItem('user-email'));
      params.append('bgno', this.gno);
      params.append('title', this.newTitle);
      params.append('content', this.newContent);
      params.append('bisNotice', this.newIsNotice);

      axios.post('writeBoard', params).then((resp) => {
        alert(resp.data.data);
        this.getBoardList();
        this.writeModal = false;
      });
    },
    getBoardList() {
      var params = new URLSearchParams();
      params.append('bgno', this.gno);
      axios
        .post('getBoardList', params)
        .then((response) => {
          this.table = response.data.notNotice;
          this.tableNotice = response.data.notice;

          for (var i = 0; i < this.table.length; i++) {
            this.table[i].writerName = response.data.notNoticeWriter[i];
            this.table[i].parsedDate = response.data.notNotice[
              i
            ].bdate.substring(2, 16);
          }

          for (i = 0; i < this.tableNotice.length; i++) {
            this.tableNotice[i].writerName = response.data.noticeWriter[i];
            this.tableNotice[i].parsedDate = response.data.notice[
              i
            ].bdate.substring(2, 16);
          }

          this.isLoadingBoardList = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getGroupInfo() {
      var params = new URLSearchParams();
      params.append('gno', this.gno);
      axios.post('getGroupInfo', params).then((response) => {
        this.groupTitle = response.data.groupInfo.gname.toUpperCase();
        this.groupDesc = response.data.groupInfo.gdesc;
        this.isLoadingGroupInfo = false;
      });
    },
    upload() {
      console.log(this.files);
      var params = new FormData();
      params.append('file', this.files);
      params.append('gno', this.gno);
      axios
        .post('fileUpload', params, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((resp) => {
          if (resp.data.result) alert('프로필 변경에 성공했습니다.');
          this.closeDialog();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    closeDialog() {
      this.dialog = false;
      this.files = [];
    },
    writeModalMethod() {
      this.newTitle = '';
      this.newContent = '';
      this.writeModal = true;
      this.newIsNotice = false;
    },
  },
};
</script>

<style>
.noticeBar {
  color: #4682b4 !important;
}
.atchBg {
  background-image: linear-gradient(
      to right,
      rgba(0, 0, 0, 0.4),
      rgba(69, 6, 99, 0.212)
    ),
    url('~@/assets/images/home.gif');
  background-position: center center;
  background-repeat: no-repeat;
  background-size: cover;
}
</style>
