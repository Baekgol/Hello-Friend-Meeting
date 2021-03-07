<template>
  <v-bottom-navigation
    v-if="$store.getters.getIsFooter"
    v-model="currentPage"
    app
    grow
    shift
    dark
    background-color="#212034"
    color="#E2B9C6"
  >
    <v-btn value="home" to="/feed/main">
      <span>홈</span>
      <v-icon color="#E8E1D5">mdi-home</v-icon>
    </v-btn>

    <v-btn value="community" to="/community">
      <span>커뮤니티</span>
      <v-icon color="#E8E1D5">mdi-account-group</v-icon>
    </v-btn>

    <v-btn value="message" to="/message">
      <span>쪽지</span>
      <v-icon color="#E8E1D5">mdi-email-check</v-icon>
    </v-btn>

    <v-btn value="info" to="/user/profile">
      <span>내정보</span>
      <v-icon color="#E8E1D5">mdi-card-account-details</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  data() {
    return {
      currentPage: 'home',
    };
  },
  computed: {
    getRouteUrl() {
      return this.$store.getters.getRouteUrl;
    },
  },
  watch: {
    getRouteUrl(routeUrl) {
      if (routeUrl == '/') {
        this.currentPage = 'home';
      } else if (routeUrl == '/group') {
        this.currentPage = 'community';
      } else if (routeUrl == '/board/detail') {
        this.currentPage = 'community';
      } else if (routeUrl == '/friendProfile') {
        this.currentPage = 'community';
      } else if (routeUrl == '/naegi') {
        this.currentPage = 'community';
      }

      this.$store.commit('setRouteUrl', null);
    },
  },
};
</script>

<style></style>
