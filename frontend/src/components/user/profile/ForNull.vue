<template>
  <v-row
    :style="'height: ' + myHeight"
    no-gutters
    justify="center"
    align="center"
  >
    <v-col align="center">
      <v-row class="myImg" style="width: 250px;" no-gutters>
        <v-img src="@/assets/images/friend.png"></v-img>
      </v-row>
      <v-row
        no-gutters
        style="font-size: 1.5rem; letter-spacing: -1px; margin: 0; margin-top: 35px;"
        justify="center"
      >
        앗, 불러올 정보가 없네요!
      </v-row>
      <v-row
        no-gutters
        style="letter-spacing: -1px; margin: 0;"
        justify="center"
      >
        더 많은 친구들을 사귀면, 이 공간은 쉽게 채워질 거예요.
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
export default {
  props: ['myHeight'],
  data() {
    return {};
  },
};
</script>

<style>
.myImg {
  -webkit-filter: grayscale(50%);
  filter: gray;
}
</style>
