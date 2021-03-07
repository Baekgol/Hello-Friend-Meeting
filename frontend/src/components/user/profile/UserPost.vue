<template>
  <v-col cols="6" style="padding-top: 0; padding-left: 0; padding-right: 0;">
    <v-subheader>내가 작성한 글</v-subheader>

    <v-list
      v-if="posts != null"
      two-line
      subheader
      flat
      height="200"
      style="overflow-y: auto;"
    >
      <v-list-item
        v-for="(post, index) in posts"
        :key="index"
        @click="movePost(index)"
        style="padding-left: 10px; padding-right: 10px; height: 50px;"
      >
        <v-list-item-avatar>
          <v-icon small>mdi-post</v-icon>
        </v-list-item-avatar>

        <v-list-item-content
          style="padding-top: 5px; padding-bottom: 5px; margin-left: -15px;"
        >
          <v-list-item-title
            style="letter-spacing: -1px;"
            v-text="post.btitle"
          ></v-list-item-title>
          <v-list-item-subtitle
            style="letter-spacing: -1px;"
            v-text="post.bdate"
          ></v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>
    </v-list>

    <v-row
      v-else
      justify="center"
      align="center"
      style="height: 200px; padding: 20px;"
      no-gutters
    >
      <v-col align-self="center" cols="3">
        <v-img
          src="@/assets/images/alarm.gif"
          style="width: 50px; height: 50px; margin-top: -15px;"
        >
        </v-img>
      </v-col>
      <v-col>
        <v-row
          no-gutters
          style="letter-spacing: -1px; padding: 0; font-size: 0.9rem; text-align: center; font-weight: bold; font-style: italic;"
        >
          이런,
        </v-row>
        <v-row
          no-gutters
          style="letter-spacing: -1px; margin-top: 0;font-size: 0.8rem; text-align: center;"
        >
          작성한 글이 없네요 :(
        </v-row>
      </v-col>
    </v-row>
  </v-col>
</template>

<script>
import axios from 'axios';

export default {
  props: ['currPosts'],
  data() {
    return {
      posts: this.currPosts,
    };
  },
  methods: {
    movePost(index) {
      let params = new URLSearchParams();

      params.append('email', window.sessionStorage.getItem('user-email'));
      params.append('gno', this.posts[index].bgno);

      axios
        .post('isGroupMember', params)
        .then((response) => {
          this.$store.commit('setGno', this.posts[index].bgno);
          this.$store.commit('setBno', this.posts[index].bno);
          this.$store.commit('setMemberStatus', response.data.memberStatus);
          this.$router.push('/board/detail');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
