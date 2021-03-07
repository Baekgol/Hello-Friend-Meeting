<template>
  <v-speed-dial
    :style="{ left: '50%', top: '160px', transform: 'translateX(-50%)' }"
    v-model="profileImgFab"
    absolute
    direction="bottom"
    transition="slide-y-transition"
  >
    <template v-slot:activator>
      <v-btn v-model="profileImgFab" fab>
        <v-avatar size="80"
          ><v-img
            :src="require(`@/assets/images/avatars/${profileImg}.png`)"
          ></v-img
        ></v-avatar>
      </v-btn>
    </template>
    <v-row justify="center" style="width: 300px; overflow-y: visible;">
      <v-btn
        v-for="(pImage, index) in pImages"
        :key="index"
        fab
        @click="changeAvatar(pImage, index)"
      >
        <v-avatar
          ><v-img
            contain
            height="100"
            :src="require(`@/assets/images/avatars/${pImage}.png`)"
          ></v-img
        ></v-avatar>
      </v-btn>
    </v-row>
  </v-speed-dial>
</template>

<script>
import axios from 'axios';

export default {
  props: ['email', 'currProfileImg', 'curr_pImages'],
  data() {
    return {
      profileImg: this.currProfileImg,
      pImages: this.curr_pImages,
      profileImgFab: false,
    };
  },
  methods: {
    changeAvatar(pImage, index) {
      let prevImg = this.profileImg;
      this.profileImg = pImage;
      this.pImages.splice(index, 1);
      this.pImages.splice(index, 0, prevImg);

      let params = new URLSearchParams();
      params.append('email', this.email);
      params.append('profileImg', pImage);

      axios
        .put('changeAvatar', params)
        .then()
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
