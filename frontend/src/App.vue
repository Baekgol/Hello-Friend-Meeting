<template>
  <v-app>
    <Header />
    <v-main :style="mainStyle">
      <router-view></router-view>
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import Header from '@/components/common/Header.vue';
import Footer from '@/components/common/Footer.vue';

export default {
  name: 'app',
  components: { Header, Footer },
  data() {
    return {
      mainStyle: 'padding: 0;',
    };
  },
  computed: {
    checkStyle() {
      return this.$store.getters.getIsHeader;
    },
  },
  watch: {
    checkStyle(isHeader) {
      if (isHeader) this.mainStyle = 'padding: 56px 0';
      else this.mainStyle = 'padding: 0;';
    },
  },
};
</script>

<style></style>
