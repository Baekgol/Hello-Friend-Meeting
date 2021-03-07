<template>
  <v-row style="margin-top: 45px;">
    <user-post v-if="!isLoadingPosts" :currPosts="posts" />
    <user-comment v-if="!isLoadingComments" :currComments="comments" />
  </v-row>
</template>

<script>
import axios from 'axios';
import UserPost from '@/components/user/profile/UserPost.vue';
import UserComment from '@/components/user/profile/UserComment.vue';

const storage = window.sessionStorage;

export default {
  components: { UserPost, UserComment },
  data() {
    return {
      posts: [],
      comments: [],
      isLoadingPosts: true,
      isLoadingComments: true,
    };
  },
  created() {
    axios
      .get('getPosts', {
        params: {
          email: storage.getItem('user-email'),
        },
      })
      .then((response) => {
        this.posts = response.data.posts;
        this.isLoadingPosts = false;
      })
      .catch((error) => {
        console.log(error);
      });

    axios
      .get('getComments', {
        params: {
          email: storage.getItem('user-email'),
        },
      })
      .then((response) => {
        this.comments = response.data.comments;
        this.isLoadingComments = false;
      })
      .catch((error) => {
        console.log(error);
      });
  },
};
</script>

<style></style>
