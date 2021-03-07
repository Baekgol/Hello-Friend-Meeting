<template>
  <v-container class="pa-0">
    <v-row class="ma-0">
      <v-tabs
        v-model="tab"
        background-color="#212034"
        centered
        dark
        grow
        color="#E2B9C6"
        icons-and-text
      >
        <v-tabs-slider></v-tabs-slider>

        <v-tab href="#tab-1" replace>
          그룹
          <v-icon color="#E8E1D5">mdi-home-group</v-icon>
        </v-tab>

        <v-tab href="#tab-2" replace>
          친구
          <v-icon color="#E8E1D5">mdi-account-group</v-icon>
        </v-tab>

        <v-tabs-items v-model="tab">
          <v-tab-item :value="'tab-1'">
            <v-row
              v-if="
                isLoadingUno ||
                  isLoadingGroups == -1 ||
                  isLoadingCategories ||
                  isLoadingFriends == -1
              "
              class="ma-0"
              style="height: 639px;"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="purple"
              ></v-progress-circular>
            </v-row>
            <group-list
              v-if="
                !isLoadingUno &&
                  isLoadingGroups != -1 &&
                  !isLoadingCategories &&
                  isLoadingFriends != -1
              "
              :uno="uno"
              :email="email"
              :groupList="groups"
              :categoryList="categories"
              :friendList="friends"
            />
          </v-tab-item>

          <v-tab-item :value="'tab-2'">
            <v-row
              v-if="
                isLoadingUno || isLoadingFriends == -1 || isLoadingCategories
              "
              class="ma-0"
              style="height: 639px;"
              align="center"
              justify="center"
            >
              <v-progress-circular
                indeterminate
                color="purple"
              ></v-progress-circular>
            </v-row>

            <friend-list
              v-if="
                !isLoadingUno && isLoadingFriends != -1 && !isLoadingCategories
              "
              :uno="uno"
              :friendList="friends"
              :favoriteFriendList="favoriteFriends"
              :categoryList="categories"
            />
          </v-tab-item>
        </v-tabs-items>
      </v-tabs>
    </v-row>
  </v-container>
</template>

<script>
import axios from 'axios';
import GroupList from '../components/community/GroupList.vue';
import FriendList from '../components/community/FriendList.vue';

const storage = window.sessionStorage;

export default {
  components: { GroupList, FriendList },
  data() {
    return {
      uno: -1,
      email: storage.getItem('user-email'),
      tab: null,
      groups: [],
      categories: [],
      friends: [],
      favoriteFriends: [],
      isLoadingUno: true,
      isLoadingGroups: -1,
      isLoadingCategories: true,
      isLoadingFriends: -1,
    };
  },
  created() {
    axios.post('findUno', { email: this.email }).then((response) => {
      if (response.data['is-success']) {
        this.uno = response.data.uno;
        this.isLoadingUno = false;
      } else alert('사용자 정보를 불러오는데 오류가 발생하였습니다.');
    });

    this.getGroupList();
    this.getCategory();
    this.getFriendList();
    this.$store.commit('setIsHeader', true);
    this.$store.commit('setIsFooter', true);
  },
  methods: {
    getGroupList() {
      axios
        .post('getGroupList', { email: this.email })
        .then((response) => {
          if (response.data['is-success']) {
            this.groups = response.data.groupList;

            for (let i = 0; i < this.groups.length; i++) {
              this.groups[i].members = this.groups[i].guserList
                .trim()
                .split(' ');

              // console.log(this.groups[i].gimg);
            }

            this.isLoadingGroups = 1;
          } else this.isLoadingGroups = 0;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getCategory() {
      axios
        .get('getCategory')
        .then((response) => {
          this.categories = response.data.categories;
          this.isLoadingCategories = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getFriendList() {
      axios
        .post('getFriendList', { email: this.email })
        .then((response) => {
          if (response.data['is-success']) {
            // 친구가 존재할 경우
            this.friends = response.data.friendList;
            this.favoriteFriends = response.data.favoriteFriendList;

            for (let i = 0; i < this.friends.length; i++) {
              for (let j = 0; j < this.favoriteFriends.length; j++) {
                if (this.friends[i].uno == this.favoriteFriends[j].uno) {
                  this.friends[i].favorite = true;
                  break;
                }

                if (j == this.favoriteFriends.length - 1)
                  this.friends[i].favorite = false;
              }
            }

            this.isLoadingFriends = 1;
          } else {
            // 친구가 존재하지 않을 경우
            this.isLoadingFriends = 0;
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  watch: {
    tab(tab) {
      if (tab == 'tab-1') {
        this.isLoadingGroups = -1;
        this.isLoadingCategories = true;
        this.isLoadingFriends = -1;
        this.groups = [];
        this.categories = [];
        this.friends = [];
        this.getGroupList();
        this.getCategory();
        this.getFriendList();
      } else {
        this.isLoadingFriends = -1;
        this.friends = [];
        this.getFriendList();
      }
    },
  },
};
</script>

<style></style>
