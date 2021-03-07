<template>
  <div>
    <v-subheader>나의 활동</v-subheader>
    <v-timeline>
      <v-timeline-item v-for="timeline in timelines" :key="timeline.tno" large>
        <template v-slot:icon>
          <v-avatar>
            <v-img
              :src="require(`@/assets/images/avatars/${profileImg}.png`)"
            />
          </v-avatar>
        </template>
        <template v-slot:opposite>
          <span>{{ parsingDate(timeline.tdate) }}</span>
        </template>
        <v-card class="elevation-2">
          <v-card-text style="text-align:center">
            {{ timeline.tcontent }}
            <strong> {{ timeline.tcontentSecond }}</strong></v-card-text
          >
        </v-card>
      </v-timeline-item>
    </v-timeline>
  </div>
</template>

<script>
export default {
  props: ['currProfileImg', 'timelines'],
  data() {
    return {
      profileImg: this.currProfileImg,
    };
  },
  methods: {
    parsingDate(beforeDate) {
      let afterDate = '';
      let month = '';
      let day = '';

      afterDate = beforeDate.substring(0, 4) + '년 ';
      month = beforeDate.substring(5, 7);
      day = beforeDate.substring(8, 10);
      afterDate = afterDate + month + '월 ' + day + '일';

      return afterDate;
    },
  },
};
</script>

<style></style>
