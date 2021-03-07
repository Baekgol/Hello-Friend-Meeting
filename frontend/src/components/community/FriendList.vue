<template>
  <v-row
    id="friendList"
    class="ma-0 overflow-y-auto"
    style="height: 639px;"
    v-scroll:#friendList="onScroll"
  >
    <!-- 상단 바 -->
    <v-toolbar
      width="100%"
      elevation="0"
      :height="toolbarHeight"
      :absolute="true"
      dense
    >
      <v-row no-gutters>
        <v-col>
          <v-row no-gutters>
            <v-col cols="2">
              <!-- 친구 추가 버튼 -->
              <v-btn
                color="success"
                fab
                dark
                depressed
                small
                @click="addFriendModal = true"
              >
                <v-icon>mdi-plus</v-icon>
              </v-btn>
            </v-col>
            <v-col>
              <v-text-field
                v-model="search"
                label="검색"
                append-icon="mdi-magnify"
                :hide-details="true"
                @input="searchInput"
                single-line
                clearable
              ></v-text-field>
            </v-col>
          </v-row>

          <v-row
            v-if="search == null || search == ''"
            style="margin-top: 14px;"
            no-gutters
          >
            <span>즐겨찾는 친구 ({{ favoriteFriends.length }})</span>
            <v-spacer></v-spacer>
            <v-btn
              v-if="isFavoriteArea || favoriteIcon == 'mdi-chevron-up'"
              small
              icon
              @click="
                favoriteIcon =
                  favoriteIcon == 'mdi-chevron-down'
                    ? 'mdi-chevron-up'
                    : 'mdi-chevron-down'
              "
              ><v-icon>{{ favoriteIcon }}</v-icon>
            </v-btn>

            <v-btn v-else small icon @click="moveFavorite"
              ><v-icon>mdi-transfer-up</v-icon>
            </v-btn>
          </v-row>

          <v-row
            v-if="(search == null || search == '') && !isFavoriteArea"
            class="mt-4"
            no-gutters
          >
            <span>친구 ({{ friends.length }})</span>
            <v-spacer></v-spacer>
            <v-btn
              small
              icon
              @click="
                allIcon =
                  allIcon == 'mdi-chevron-down'
                    ? 'mdi-chevron-up'
                    : 'mdi-chevron-down'
              "
            >
              <v-icon>{{ allIcon }}</v-icon>
            </v-btn>
          </v-row>

          <v-row v-if="search != null && search != ''" class="mt-0" no-gutters>
            <span>검색 결과 ({{ searchFriends.length }})</span>
          </v-row>
        </v-col>
      </v-row>
    </v-toolbar>

    <!-- 친구 목록 -->
    <v-col v-if="search == null || search == ''" class="pa-0">
      <transition name="list-transition">
        <v-list
          v-if="favoriteIcon == 'mdi-chevron-down'"
          class="py-0"
          style="margin-top: 100px;"
          width="100%"
        >
          <template v-for="(favoriteFriend, index) in favoriteFriends">
            <v-list-item :key="'favorite-' + index">
              <v-list-item-avatar
                @click="favoriteFriendListSelect(favoriteFriend)"
              >
                <v-img
                  :src="
                    require(`@/assets/images/avatars/${favoriteFriend.uprofileImg}.png`)
                  "
                ></v-img>
              </v-list-item-avatar>

              <v-list-item-content
                @click="favoriteFriendListSelect(favoriteFriend)"
              >
                <v-list-item-title
                  v-text="favoriteFriend.uname"
                ></v-list-item-title>
              </v-list-item-content>

              <v-list-item-icon @click="favoriteFriendFavoriteChange(index)">
                <v-icon color="blue accent-4">mdi-star</v-icon>
              </v-list-item-icon>
            </v-list-item>
          </template>
        </v-list>
      </transition>

      <v-row
        id="allHeader"
        class="px-4"
        style="padding-bottom: 11px;"
        :style="
          favoriteIcon == 'mdi-chevron-up'
            ? 'margin-top: 100px; padding-top: 5px;'
            : 'margin-top: 32px;'
        "
        no-gutters
      >
        <span>친구 ({{ friends.length }})</span>
        <v-spacer></v-spacer>
        <v-btn
          small
          icon
          @click="
            allIcon =
              allIcon == 'mdi-chevron-down'
                ? 'mdi-chevron-up'
                : 'mdi-chevron-down'
          "
        >
          <v-icon>{{ allIcon }}</v-icon>
        </v-btn>
      </v-row>

      <transition name="list-transition">
        <v-list
          v-if="allIcon == 'mdi-chevron-down'"
          id="allList"
          class="py-0"
          width="100%"
        >
          <template v-for="(friend, index) in friends">
            <v-list-item :key="'all-' + index">
              <v-list-item-avatar @click="allFriendListSelect(friend)">
                <v-img
                  :src="
                    require(`@/assets/images/avatars/${friend.uprofileImg}.png`)
                  "
                ></v-img>
              </v-list-item-avatar>

              <v-list-item-content @click="allFriendListSelect(friend)">
                <v-list-item-title v-text="friend.uname"></v-list-item-title>
              </v-list-item-content>

              <v-list-item-icon @click="allFriendFavoriteChange(index)">
                <v-icon
                  color="blue accent-4"
                  v-text="friend.favorite ? 'mdi-star' : 'mdi-star-outline'"
                ></v-icon>
              </v-list-item-icon>
            </v-list-item>
          </template>
        </v-list>
      </transition>
    </v-col>

    <!-- 검색 결과 -->
    <v-col v-else class="pa-0">
      <transition name="list-transition">
        <v-list class="py-0" style="margin-top: 100px;" width="100%">
          <template v-for="(searchFriend, index) in searchFriends">
            <v-list-item :key="'search-' + index">
              <v-list-item-avatar @click="searchFriendListSelect(searchFriend)">
                <v-img
                  :src="
                    require(`@/assets/images/avatars/${searchFriend.uprofileImg}.png`)
                  "
                ></v-img>
              </v-list-item-avatar>

              <v-list-item-content
                @click="searchFriendListSelect(searchFriend)"
              >
                <v-list-item-title
                  v-text="searchFriend.uname"
                ></v-list-item-title>
              </v-list-item-content>

              <v-list-item-icon @click="searchFriendFavoriteChange(index)">
                <v-icon
                  color="blue accent-4"
                  v-text="
                    searchFriend.favorite ? 'mdi-star' : 'mdi-star-outline'
                  "
                ></v-icon>
              </v-list-item-icon>
            </v-list-item>
          </template>
        </v-list>
      </transition>
    </v-col>

    <!-- 친구 프로필 모달 -->
    <v-dialog
      v-if="friendInfo != null"
      v-model="friendProfileModal"
      persistent
      scrollable
    >
      <v-card>
        <v-card-title>
          <span
            ><strong>{{ friendInfo.uname }}</strong
            >님의 프로필</span
          >
          <v-spacer></v-spacer>
          <v-btn icon @click="friendProfileModal = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text>
          <friend-profile
            :uno="uno"
            :info="friendInfo"
            :categoryList="categoryList"
            @deleteFriend="deleteFriend"
          />
        </v-card-text>
        <v-card-actions> </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 친구 추가 모달 -->
    <v-dialog v-model="addFriendModal" persistent>
      <v-card>
        <v-card-title>
          <span>친구 추가</span>
          <v-spacer></v-spacer>
          <v-btn icon @click="addFriendModal = false">
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>
        <v-card-text class="pb-0">
          <v-row no-gutters>
            <v-text-field
              v-model="friendTel"
              placeholder="핸드폰 번호를 입력해주세요."
              hint="- 제외하고 입력"
              clearable
              required
            ></v-text-field>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-row justify="end" no-gutters>
            <v-btn
              color="success"
              :disabled="!addFriendValid"
              @click="addFriendSearchModal = true"
              >검색하기</v-btn
            >
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 친구 추가 검색 결과 모달 -->
    <v-dialog v-model="addFriendSearchModal" width="270" persistent>
      <v-card>
        <v-card-title>
          <span>친구 검색 결과</span>
        </v-card-title>
        <v-card-text class="pb-0">
          <v-row v-if="addFriendInfo != null" no-gutters>
            <v-col>
              <v-list-item class="px-0">
                <v-list-item-avatar>
                  <v-img
                    :src="
                      require(`@/assets/images/avatars/${addFriendInfo.uprofileImg}.png`)
                    "
                  ></v-img>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title
                    v-text="addFriendInfo.uname"
                  ></v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-col>
          </v-row>

          <v-row v-else justify="center" no-gutters>
            <v-progress-circular
              indeterminate
              color="purple"
            ></v-progress-circular>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-row justify="end" no-gutters>
            <v-btn
              color="error"
              class="font-weight-black"
              style="margin-right: 10px;"
              :disabled="!addFriendValid"
              @click="addFriend"
            >
              확인
            </v-btn>
            <v-btn color="warning" @click="addFriendSearchModal = false">
              취소
            </v-btn>
          </v-row>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import axios from 'axios';
import FriendProfile from '@/components/user/profile/FriendProfile.vue';

const storage = window.sessionStorage;

export default {
  components: { FriendProfile },
  props: ['uno', 'friendList', 'favoriteFriendList', 'categoryList'],
  data() {
    return {
      friends: this.friendList,
      favoriteFriends: this.favoriteFriendList,
      searchFriends: [],
      toolbarHeight: 100,
      search: null,
      isFavoriteArea: true,
      favoriteIcon: 'mdi-chevron-down',
      allHeaderTop: 0,
      allIcon: 'mdi-chevron-down',
      friendProfileModal: false,
      friendInfo: null,
      addFriendModal: false,
      myTel: '',
      friendTel: '',
      addFriendValid: false,
      addFriendSearchModal: false,
      addFriendInfo: null,
    };
  },
  created() {
    axios
      .post('findUserByUno', { uno: this.uno })
      .then((response) => {
        this.myTel = response.data.user.tel;
      })
      .catch((error) => {
        console.log(error);
      });
  },
  mounted() {
    this.getAllHeaderTop();
  },
  methods: {
    onScroll(e) {
      if (this.search != null && this.search != '') {
        return;
      } else if (
        this.isFavoriteArea &&
        e.target.scrollTop >= this.allHeaderTop
      ) {
        this.isFavoriteArea = false;
        this.toolbarHeight = 150;
      } else if (
        !this.isFavoriteArea &&
        e.target.scrollTop < this.allHeaderTop
      ) {
        this.isFavoriteArea = true;
        this.toolbarHeight = 100;
      }
    },
    getAllHeaderTop() {
      this.$nextTick(function() {
        this.allHeaderTop =
          document.querySelector('#allHeader').getBoundingClientRect().top -
          150 -
          56 -
          72 +
          28;
      });
    },
    moveFavorite() {
      document.querySelector('#friendList').scrollTop = 0;
    },
    searchInput() {
      this.searchFriends = [];

      if (this.search == null || this.search == '') {
        this.favoriteIcon = 'mdi-chevron-down';
        this.allIcon = 'mdi-chevron-down';
        this.isFavoriteArea = true;
      } else {
        for (let i = 0; i < this.friends.length; i++) {
          if (this.friends[i].uname.includes(this.search))
            this.searchFriends.push(this.friends[i]);
        }
      }

      this.toolbarHeight = 100;
      document.querySelector('#friendList').scrollTop = 0;
    },
    favoriteFriendListSelect(favoriteFriend) {
      this.friendInfo = favoriteFriend;
      this.friendProfileModal = true;
    },
    allFriendListSelect(friend) {
      this.friendInfo = friend;
      this.friendProfileModal = true;
    },
    searchFriendListSelect(searchFriend) {
      this.friendInfo = searchFriend;
      this.friendProfileModal = true;
    },
    isFavorite(friendUno) {
      for (let i = 0; i < this.favoriteFriends.length; i++) {
        if (friendUno == this.favoriteFriends[i].uno) return true;
      }

      return false;
    },
    allFriendFavoriteChange(index) {
      this.friends[index].favorite = !this.friends[index].favorite;
      this.$set(this.friends, index, this.friends[index]);

      if (this.friends[index].favorite) {
        if (this.favoriteFriends == null || this.favoriteFriends.length == 0) {
          this.favoriteFriends.splice(0, 0, this.friends[index]);
          this.allHeaderTop += 56;
        } else {
          let size = this.favoriteFriends.length;

          for (let i = 0; i < size; i++) {
            if (this.favoriteFriends[i].uno > this.friends[index].uno) {
              this.favoriteFriends.splice(i, 0, this.friends[index]);
              this.allHeaderTop += 56;
              break;
            }

            if (i == this.favoriteFriends.length - 1) {
              this.favoriteFriends.push(this.friends[index]);
              this.allHeaderTop += 56;
            }
          }
        }
      } else {
        for (let i = 0; i < this.favoriteFriends.length; i++) {
          if (this.favoriteFriends[i].uno == this.friends[index].uno) {
            this.favoriteFriends.splice(i, 1);
            this.allHeaderTop -= 56;
            break;
          }
        }
      }

      axios
        .put('favoriteChange', {
          email: storage.getItem('user-email'),
          friendUno: this.friends[index].uno,
          isFavorite: this.friends[index].favorite,
        })
        .then((response) => {
          if (!response.data['is-success']) {
            alert('즐겨찾기 변경을 실패하였습니다.');
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    favoriteFriendFavoriteChange(index) {
      for (let i = 0; i < this.friends.length; i++) {
        if (this.friends[i].uno == this.favoriteFriends[index].uno) {
          this.friends[i].favorite = false;
          this.$set(this.friends, i, this.friends[i]);
          this.favoriteFriends.splice(index, 1);

          axios
            .put('favoriteChange', {
              email: storage.getItem('user-email'),
              friendUno: this.friends[i].uno,
              isFavorite: false,
            })
            .then((response) => {
              if (!response.data['is-success']) {
                alert('즐겨찾기 변경을 실패하였습니다.');
              }
            })
            .catch((error) => {
              console.log(error);
            });

          break;
        }
      }
    },
    searchFriendFavoriteChange(index) {
      this.searchFriends[index].favorite = !this.searchFriends[index].favorite;
      this.$set(this.searchFriends, index, this.searchFriends[index]);

      let idx = -1;

      for (let i = 0; i < this.friends.length; i++) {
        if (this.friends[i].uno == this.searchFriends[index].uno) {
          idx = i;
          break;
        }
      }

      if (this.friends[idx].favorite) {
        if (this.favoriteFriends == null || this.favoriteFriends.length == 0) {
          this.favoriteFriends.splice(0, 0, this.friends[idx]);
          this.allHeaderTop += 56;
        } else {
          let size = this.favoriteFriends.length;

          for (let i = 0; i < size; i++) {
            if (this.favoriteFriends[i].uno > this.friends[idx].uno) {
              this.favoriteFriends.splice(i, 0, this.friends[idx]);
              this.allHeaderTop += 56;
              break;
            }

            if (i == this.favoriteFriends.length - 1) {
              this.favoriteFriends.push(this.friends[idx]);
              this.allHeaderTop += 56;
            }
          }
        }
      } else {
        for (let i = 0; i < this.favoriteFriends.length; i++) {
          if (this.favoriteFriends[i].uno == this.friends[idx].uno) {
            this.favoriteFriends.splice(i, 1);
            this.allHeaderTop -= 56;
            break;
          }
        }
      }

      axios
        .put('favoriteChange', {
          email: storage.getItem('user-email'),
          friendUno: this.friends[idx].uno,
          isFavorite: this.friends[idx].favorite,
        })
        .then((response) => {
          if (!response.data['is-success']) {
            alert('즐겨찾기 변경을 실패하였습니다.');
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    deleteFriend(friendUno) {
      for (let i = 0; i < this.friends.length; i++) {
        if (this.friends[i].uno == friendUno) {
          this.friends.splice(i, 1);
          break;
        }
      }

      for (let i = 0; i < this.favoriteFriends.length; i++) {
        if (this.favoriteFriends[i].uno == friendUno) {
          this.favoriteFriends.splice(i, 1);
          this.allHeaderTop -= 56;
          break;
        }
      }

      for (let i = 0; i < this.searchFriends.length; i++) {
        if (this.searchFriends[i].uno == friendUno) {
          this.searchFriends.splice(i, 1);
          break;
        }
      }

      this.friendProfileModal = false;
    },
    addFriend() {
      axios
        .post('addFriendByTel', {
          myUno: this.uno,
          friendUno: this.addFriendInfo.uno,
        })
        .then((response) => {
          let isSuccess = response.data['is-success'];

          if (isSuccess == 0) {
            alert(this.addFriendInfo.uname + '님께 친구 요청을 보냈습니다.');
            this.addFriendSearchModal = false;
            this.addFriendModal = false;
          } else if (isSuccess == 1) alert('이미 친구 요청을 보냈습니다.');
          else if (isSuccess == 2) {
            alert('상대방이 이미 친구 요청을 보냈습니다. 알림을 확인해주세요.');
          } else if (isSuccess == 3) alert('이미 친구로 추가된 사용자입니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  watch: {
    favoriteIcon(favoriteIcon) {
      if (favoriteIcon == 'mdi-chevron-down') {
        this.isFavoriteArea = true;
        this.toolbarHeight = 100;
      }

      this.getAllHeaderTop();

      if (favoriteIcon == 'mdi-chevron-down')
        document.querySelector('#friendList').scrollTop = 0;
      else
        this.allHeaderTop = document.querySelector(
          '#friendList'
        ).scrollTop = this.allHeaderTop;
    },
    allIcon(allIcon) {
      if (allIcon == 'mdi-chevron-down') {
        if (this.favoriteIcon == 'mdi-chevron-up') this.getAllHeaderTop();

        this.$nextTick(function() {
          document.querySelector('#friendList').scrollTop =
            this.allHeaderTop + 5;
        });
      }
    },
    friendProfileModal(friendProfileModal) {
      if (!friendProfileModal) this.friendInfo = null;
    },
    addFriendModal(addFriendModal) {
      if (!addFriendModal) this.friendTel = '';
    },
    friendTel(friendTel) {
      if (friendTel != null && friendTel.length > 0) this.addFriendValid = true;
      else this.addFriendValid = false;
    },
    addFriendSearchModal(addFriendSearchModal) {
      if (addFriendSearchModal) {
        if (this.myTel == this.friendTel) {
          alert('자신의 번호를 입력하였습니다.');
          this.addFriendSearchModal = false;
        } else {
          axios
            .post('findUserByTel', { tel: this.friendTel })
            .then((response) => {
              if (response.data['is-success'])
                this.addFriendInfo = response.data.user;
              else {
                alert('친구를 발견하지 못하였습니다.');
                this.addFriendSearchModal = false;
              }
            })
            .catch((error) => {
              console.log(error);
            });
        }
      } else this.addFriendInfo = null;
    },
  },
};
</script>

<style>
.list-transition-enter {
  opacity: 0;
}

.list-transition-enter-active {
  transition: all 0.2s linear;
}

.list-transition-enter-to {
  transform: translateY(10px);
  opacity: 1;
}

.list-transition-leave-active {
  transition: all 0.2s linear;
}

.list-transition-leave-to {
  transform: translateY(-10px);
  opacity: 0;
}
</style>
