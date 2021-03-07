<template>
  <div v-if="!isLoadingPost">
    <v-parallax
      height="300"
      src="https://cdn.vuetifyjs.com/images/cards/sunshine.jpg"
    >
      <v-btn
        icon
        x-large
        @click="returnToGroup"
        color="white"
        style="margin-left: -15px;"
      >
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-row class="for-title" style="margin-top: 160px;">
        <v-col>
          <v-row
            style="margin-top: 0; margin-right: 10px;"
            no-gutters
            justify="end"
          >
            <span style="color: white; letter-spacing: -1px; font-size: 1rem;">
              {{ parsedDateforArticle }}
            </span>
          </v-row>
          <!-- <v-row style="margin-top: -10px; margin-left: 10px;" no-gutters>
            <span
              style="color: white; letter-spacing: -2px; font-size: 3rem; font-weight: bold;"
            >
              {{ article.btitle }}
            </span>
          </v-row> -->
          <v-row
            no-gutters
            style="margin-top: -10px; margin-right: 10px;"
            justify="end"
          >
            <span
              style="color: white; letter-spacing: -1px; font-size: 1.5rem; font-style: italic;"
            >
              {{ writer }}
            </span>
            <span
              style="color: white; margin-top: 8px; margin-left: 5px; letter-spacing: -1px; font-size: 1.1rem;"
            >
              님이 작성한 글입니다.
            </span>
          </v-row>
        </v-col>
      </v-row>
    </v-parallax>

    <!-- 글 영역 -->
    <v-card-title
      style="margin: 10px; padding-top: 20px; letter-spacing: -2px; font-size: 1.8rem; font-weight: bold; "
    >
      {{ article.btitle }}
    </v-card-title>
    <v-card-subtitle
      style="letter-spacing: -1px; font-size: 1rem; margin-top: -20px; margin-bottom: 10px; padding: 20px; "
    >
      {{ article.bcontent }}
    </v-card-subtitle>

    <v-card-actions style="padding: 10px; margin-right: 10px;">
      <v-spacer></v-spacer>
      <v-btn
        text
        v-if="localIsWriter"
        @click="boardModifyOpen"
        @hide="boardModify = false"
        >EDIT</v-btn
      >
      <v-btn
        color="purple"
        text
        v-if="localIsWriter || memberStatus === 4"
        @click="delBoard"
      >
        DELETE
      </v-btn>
    </v-card-actions>
    <b-modal
      v-model="boardModify"
      centered
      id="modal-modify"
      title="글 수정"
      @ok="modifyBoard"
      >제목 : <input type="text" v-model="newTitle" />
      <b-form-textarea
        id="textarea-rows"
        rows="8"
        v-model="newContent"
      ></b-form-textarea>
    </b-modal>

    <!-- 댓글 작성 -->
    <v-row
      style="font-size: 1.5rem; margin: 0; padding-left: 10px; margin-top: 10px;"
    >
      <v-icon>mdi-comment-multiple-outline</v-icon>
      <span style="margin-left: 5px; letter-spacing: -1px;">Comments</span>
    </v-row>
    <v-row no-gutters justify="center">
      <v-col style="margin-left: 15px;" align="center">
        <v-text-field
          :dense="true"
          v-model="newCommentContent"
          outlined
          required
          label="착한 댓글"
          @keyup.enter="writeComment"
        >
        </v-text-field>
      </v-col>
      <v-col cols="3" align="center">
        <v-btn @click="writeComment">등 록</v-btn>
      </v-col>
    </v-row>

    <!-- 댓글 리스트 -->

    <v-row style="margin: 0; margin-top: -15px;">
      <v-col style="padding: 0;">
        <v-simple-table>
          <template v-slot:default>
            <tbody>
              <tr
                style="height: 50px;"
                v-for="comment in comments"
                :key="comment.cno"
              >
                <td
                  class="text-left"
                  style="padding: 0; padding-left: 10px; width: 80px;"
                >
                  <p style="font-size: 1rem; margin: 0; letter-spacing: -1px;">
                    {{ comment.writerName }}
                  </p>
                  <p
                    style="font-size: 0.7rem; color: #B0C4DE; margin: 0; letter-spacing: -1px;"
                  >
                    {{ comment.parsedDate }}
                  </p>
                </td>
                <td class="text-left">
                  <v-row no-gutters style="margin-top: 5px;">
                    <p
                      v-if="!editModal"
                      style="font-size: 1rem; margin: 0; letter-spacing: -1px;"
                    >
                      {{ comment.ccontent }}
                    </p>
                    <p
                      style="font-size: 1rem; margin: 0; letter-spacing: -1px;"
                      v-else
                    >
                      <v-textarea
                        rows="1"
                        auto-grow
                        filled
                        v-model="modifiedCommentContent"
                      />
                    </p>
                  </v-row>
                  <v-row
                    no-gutters
                    style="margin-top: -10px;"
                    justify="end"
                    v-if="comment.isWriter"
                  >
                    <v-col cols="3">
                      <v-btn
                        icon
                        @click="commentToInput(comment.ccontent)"
                        v-if="comment.isWriter && !editModal"
                        >수정</v-btn
                      >
                      <v-btn
                        icon
                        @click="modifyComment(comment.cno)"
                        v-if="comment.isWriter && editModal"
                        >완료</v-btn
                      >

                      <v-btn
                        icon
                        @click="delComment(comment.cno)"
                        v-if="
                          (comment.isWriter || memberStatus === 4) && !editModal
                        "
                        >삭제</v-btn
                      >
                      <v-btn
                        icon
                        @click="editModal = false"
                        v-if="
                          (comment.isWriter || memberStatus === 4) && editModal
                        "
                        >취소</v-btn
                      >
                    </v-col>
                  </v-row>
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-col>
    </v-row>
  </div>

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
import axios from "axios";
export default {
  computed: {
    vuexGno() {
      return this.$store.getters.getGno;
    },
    vuexBno() {
      return this.$store.getters.getBno;
    },
    vuexMemberStatus() {
      return this.$store.getters.getMemberStatus;
    },
    isWriter() {
      return this.$store.getters.getIsWriter;
    },
  },
  watch: {
    vuexGno(val) {
      this.gno = val;
    },
    vuexBno(val) {
      this.bno = val;
      this.getBoardDetail();
    },
    vuexMemberStatus(val) {
      this.memberStatus = val;
    },
    isWriter(val) {
      this.localIsWriter = val;
    },
  },
  data() {
    return {
      memberStatus: this.$store.getters.getMemberStatus,
      gno: this.$store.getters.getGno,
      bno: this.$store.getters.getBno,
      localIsWriter: this.$store.getters.getIsWriter,
      article: {},
      writer: "",
      parsedDateforArticle: "",
      comments: [],
      newCommentContent: "",
      newTitle: "",
      newContent: "",
      modifiedCommentContent: "",
      show: false,
      editModal: false,
      isLoadingPost: true,
      boardModify: false,
    };
  },
  created() {
    this.getBoardDetail();
  },
  methods: {
    getBoardDetail() {
      var params = new URLSearchParams();
      params.append("bno", this.bno);
      params.append("email", window.sessionStorage.getItem("user-email"));
      axios
        .post("boardDetail", params)
        .then((response) => {
          if (!response.data.isExist) {
            alert("삭제된 게시글입니다.");
            this.$router.push("/");
            return;
          }
          this.article = response.data.curBoard;
          this.parsedDateforArticle = this.parsingDate(
            response.data.curBoard.bdate
          );
          response.data.curBoard.bdate.substring(2, 3);
          this.writer = response.data.writer.uname;
          this.localIsWriter = response.data.isWriter;
          this.newTitle = this.article.btitle;
          this.newContent = this.article.bcontent;
          this.getComments();
        })
        .catch((error) => {
          error;
        });
    },
    parsingDate(beforeDate) {
      let afterDate = "";
      let month = "";
      let day = "";

      afterDate = beforeDate.substring(2, 4) + "년 ";

      if (beforeDate.charAt(5) == "0") {
        month = beforeDate.charAt(6);
      } else {
        month = beforeDate.substring(5, 7);
      }

      if (beforeDate.charAt(8) == "0") {
        day = beforeDate.charAt(9);
      } else {
        day = beforeDate.substring(8, 10);
      }

      afterDate = afterDate + month + "월 " + day + "일";

      return afterDate;
    },
    getComments() {
      var params = new URLSearchParams();
      params.append("bno", this.bno);
      params.append("email", window.sessionStorage.getItem("user-email"));
      axios.post("getCommentList", params).then((resp) => {
        this.comments = resp.data.comments;
        var writerList = resp.data.writerList;
        var isWriterList = resp.data.isWriterList;

        for (var i = 0; i < this.comments.length; i++) {
          this.comments[i].writerName = writerList[i];
          this.comments[i].isWriter = isWriterList[i];
          this.comments[i].parsedDate = resp.data.comments[i].cdate.substring(
            2,
            16
          );
        }

        this.isLoadingPost = false;
      });
    },
    returnToGroup() {
      var gno = this.article.bgno;
      this.$router.push({ name: "GroupMainPage", params: { gno } });
    },
    writeComment() {
      if (this.newCommentContent != "") {
        var params = new URLSearchParams();
        params.append("email", window.sessionStorage.getItem("user-email"));
        params.append("ccontent", this.newCommentContent);
        params.append("bno", this.bno);

        axios.post("writeComment", params).then((response) => {
          this.getComments();
          this.resetDatas();
        });
        this.newCommentContent = "";
      }
    },
    modifyComment(cno) {
      var params = new URLSearchParams();
      params.append("cno", cno);
      params.append("ccontent", this.modifiedCommentContent);
      axios.post("modifyComment", params).then((resp) => {
        this.getComments();
        this.editModal = false;
      });
    },
    delComment(cno) {
      var params = new URLSearchParams();
      params.append("cno", cno);
      axios.post("delComment", params).then((resp) => {
        this.getComments();
      });
    },
    delBoard() {
      var params = new URLSearchParams();
      params.append("bno", this.bno);
      axios.post("delBoard", params).then((resp) => {
        alert(resp.data.data);
        this.$store.commit("setGno", this.gno);
        this.$router.push("/group");
      });
    },
    modifyBoard(modal) {
      if (this.newTitle === "") {
        modal.preventDefault();
        alert("제목을 입력해주세요!");
        return;
      } else if (this.newContent === "") {
        modal.preventDefault();
        alert("내용을 입력해주세요!");
        return;
      }

      var params = new URLSearchParams();
      params.append("bno", this.bno);
      params.append("title", this.newTitle);
      params.append("content", this.newContent);
      axios.post("modifyBoard", params).then((resp) => {
        this.getBoardDetail();
        alert(resp.data.data);
      });
    },
    boardModifyOpen() {
      this.boardModify = true;
      this.newTitle = this.article.btitle;
      this.newContent = this.article.bcontent;
    },
    commentToInput(ccontent) {
      this.editModal = true;
      this.modifiedCommentContent = ccontent;
    },
  },
};
</script>

<style></style>
