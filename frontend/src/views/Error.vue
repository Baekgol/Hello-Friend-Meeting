<template>
  <v-container class="pa-0">
    <v-row style="height: 823px;" align="center" no-gutters>
      <v-col class="text-center">
        <v-img src="@/assets/images/error.png" height="100" contain> </v-img>
        <h4 style="margin-top: 30px;">
          <strong>해당 페이지에 오류가 발생했습니다.</strong>
        </h4>
        <v-divider></v-divider>
        <router-link to="/">
          <v-btn color="primary">돌아가기</v-btn>
        </router-link>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  created() {
    this.$store.commit('setIsHeader', false);
    this.$store.commit('setIsFooter', false);
  },
};
</script>

<style></style>
