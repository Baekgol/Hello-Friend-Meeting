<template>
  <v-row no-gutters>
    <v-col>
      <v-subheader class="px-0">친구가 활동하는 그룹</v-subheader>
      <v-expansion-panels
        v-if="isLoadingGroups == 1"
        v-model="panel"
        class="px-4 py-2 overflow-y-auto"
        :style="panelsStyle"
        ref="panels"
      >
        <v-expansion-panel
          v-for="(group, index) in groups"
          :key="index"
          ref="group"
        >
          <v-expansion-panel-header class="py-0">
            <v-list-item class="px-0">
              <v-list-item-avatar>
                <v-avatar>
                  <img :src="'data:image/png;base64,' + group.gimg" />
                </v-avatar>
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title class="subtitle-2">{{
                  group.gname
                }}</v-list-item-title>
                <v-list-item-subtitle>{{
                  group.categoryName
                }}</v-list-item-subtitle>
              </v-list-item-content>
            </v-list-item>
            <template v-slot:actions>
              <v-icon color="purpl" @click="scrollGroup(index)">$expand</v-icon>
            </template>
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            <v-row no-gutters>
              {{ group.gdesc }}
            </v-row>
            <v-row no-gutters>
              <v-btn color="success" @click="applyGroup(group)" block
                >그룹 신청</v-btn
              >
            </v-row>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>

      <v-row
        v-else-if="isLoadingGroups == 0"
        class="ma-0"
        style="height: 250px;"
        align="center"
        justify="center"
      >
        <v-icon color="red" size="50">mdi-close-circle</v-icon>
      </v-row>

      <v-row
        v-else
        class="ma-0"
        style="height: 250px;"
        align="center"
        justify="center"
      >
        <v-progress-circular indeterminate color="purple"></v-progress-circular>
      </v-row>
    </v-col>
  </v-row>
</template>

<script>
import axios from "axios";

export default {
  props: ["uno", "info", "categoryList", "panelsHeight"],
  data() {
    return {
      groups: [],
      isLoadingGroups: -1,
      panel: null,
      panelsStyle: "",
    };
  },
  created() {
    this.panelsStyle = "max-height: " + this.panelsHeight + "px;";

    axios
      .get("getGroupList/" + this.info.uno)
      .then((response) => {
        if (response.data["is-success"]) {
          this.groups = response.data.groupList;
          for (let i = 0; i < this.groups.length; i++) {
            this.groups[i].members = this.groups[i].guserList.trim().split(" ");

            for (let j = 0; j < this.categoryList.length; j++) {
              if (this.categoryList[j].cno == this.groups[i].gcategory) {
                this.groups[i].categoryName = this.categoryList[j].cname;
                break;
              }
            }
          }

          this.isLoadingGroups = 1;
        } else this.isLoadingGroups = 0;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  methods: {
    scrollGroup(index) {
      this.$nextTick(function() {
        if (index == 0) this.$refs.panels.$el.scrollTop = 0;
        else
          this.$refs.panels.$el.scrollTop = this.$refs["group"][
            index
          ].$el.offsetTop;
      });
    },
    applyGroup(group) {
      axios
        .post("applyGroup", { uno: this.uno, gno: group.gno })
        .then((response) => {
          let isSuccess = response.data["is-success"];

          if (isSuccess == 1) alert("그룹에 가입 신청을 보냈습니다.");
          else if (isSuccess == 2) alert("이미 신청한 그룹입니다.");
          else if (isSuccess == 3) alert("이미 가입된 그룹입니다.");
          else alert("그룹 신청을 하는데 오류가 발생하였습니다.");
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style></style>
