<template>
  <v-row class="ma-0" style="height: 639px;" no-gutters>
    <v-col>
      <!-- 상단 바 -->
      <v-toolbar height="70" elevation="0" dense>
        <v-row no-gutters>
          <v-col cols="2">
            <v-btn
              color="success"
              @click="createGroupModal = true"
              fab
              dark
              depressed
              small
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
      </v-toolbar>
      <v-row
        v-if="groups.length != 0"
        class="pa-4 overflow-y-auto"
        style="height: 591px;"
        no-gutters
      >
        <!-- 그룹 목록 -->
        <v-col v-if="search == null || search == ''">
          <v-row v-for="row in groupListRow" :key="'row-' + row" no-gutters>
            <template v-if="row != groupListRow">
              <v-col
                v-for="col in 4"
                :key="'col-' + 4 * (row - 1) + (col - 1)"
                cols="3"
              >
                <v-row justify="center" no-gutters>
                  <v-col align="center">
                    <v-badge color="rgb(255, 255, 255, 0)" avatar overlap left>
                      <template v-slot:badge>
                        <v-avatar
                          v-if="
                            groups[4 * (row - 1) + (col - 1)].gmaster == uno
                          "
                        >
                          <v-icon color="amber accent-4">mdi-crown</v-icon>
                        </v-avatar>
                      </template>

                      <v-btn
                        @click="moveGroup(4 * (row - 1) + (col - 1), false)"
                        depressed
                        fab
                      >
                        <v-avatar size="60">
                          <v-img
                            :src="
                              'data:image/png;base64,' +
                                groups[4 * (row - 1) + (col - 1)].gimg
                            "
                          ></v-img>
                        </v-avatar>
                      </v-btn>
                    </v-badge>
                    <p class="text-truncate">
                      {{ groups[4 * (row - 1) + (col - 1)].gname }}
                    </p>
                  </v-col>
                </v-row>
              </v-col>
            </template>
            <template v-else>
              <v-col
                v-for="col in groups.length % 4"
                :key="'col-' + 4 * (row - 1) + (col - 1)"
                cols="3"
              >
                <v-row justify="center" no-gutters>
                  <v-col align="center">
                    <v-badge color="rgb(255, 255, 255, 0)" avatar overlap left>
                      <template v-slot:badge>
                        <v-avatar
                          v-if="
                            groups[4 * (row - 1) + (col - 1)].gmaster == uno
                          "
                        >
                          <v-icon color="amber accent-4">mdi-crown</v-icon>
                        </v-avatar>
                      </template>

                      <v-btn
                        @click="moveGroup(4 * (row - 1) + (col - 1), false)"
                        depressed
                        fab
                      >
                        <v-avatar size="60">
                          <v-img
                            :src="
                              'data:image/png;base64,' +
                                groups[4 * (row - 1) + (col - 1)].gimg
                            "
                          ></v-img>
                        </v-avatar>
                      </v-btn>
                    </v-badge>
                    <p class="text-truncate">
                      {{ groups[4 * (row - 1) + (col - 1)].gname }}
                    </p>
                  </v-col>
                </v-row>
              </v-col>
            </template>
          </v-row>
        </v-col>

        <!-- 검색 결과 -->
        <v-col v-else>
          <v-row
            v-for="row in searchGroupListRow"
            :key="'row-' + row"
            no-gutters
          >
            <template v-if="row != searchGroupListRow">
              <v-col
                v-for="col in 4"
                :key="'col-' + 4 * (row - 1) + (col - 1)"
                cols="3"
              >
                <v-row justify="center" no-gutters>
                  <v-col align="center">
                    <v-badge color="rgb(255, 255, 255, 0)" avatar overlap left>
                      <template v-slot:badge>
                        <v-avatar
                          v-if="
                            searchGroups[4 * (row - 1) + (col - 1)].gmaster ==
                              uno
                          "
                        >
                          <v-icon color="amber accent-4">mdi-crown</v-icon>
                        </v-avatar>
                      </template>

                      <v-btn
                        @click="moveGroup(4 * (row - 1) + (col - 1), true)"
                        depressed
                        fab
                      >
                        <v-avatar size="60">
                          <v-img
                            :src="
                              'data:image/png;base64,' +
                                searchGroups[4 * (row - 1) + (col - 1)].gimg
                            "
                          ></v-img>
                        </v-avatar>
                      </v-btn>
                    </v-badge>
                    <p class="text-truncate">
                      {{ searchGroups[4 * (row - 1) + (col - 1)].gname }}
                    </p>
                  </v-col>
                </v-row>
              </v-col>
            </template>
            <template v-else>
              <v-col
                v-for="col in searchGroups.length % 4"
                :key="'col-' + 4 * (row - 1) + (col - 1)"
                cols="3"
              >
                <v-row justify="center" no-gutters>
                  <v-col align="center">
                    <v-badge color="rgb(255, 255, 255, 0)" avatar overlap left>
                      <template v-slot:badge>
                        <v-avatar
                          v-if="
                            searchGroups[4 * (row - 1) + (col - 1)].gmaster ==
                              uno
                          "
                        >
                          <v-icon color="amber accent-4">mdi-crown</v-icon>
                        </v-avatar>
                      </template>

                      <v-btn
                        @click="moveGroup(4 * (row - 1) + (col - 1), true)"
                        depressed
                        fab
                      >
                        <v-avatar size="60">
                          <v-img
                            :src="
                              'data:image/png;base64,' +
                                searchGroups[4 * (row - 1) + (col - 1)].gimg
                            "
                          ></v-img>
                        </v-avatar>
                      </v-btn>
                    </v-badge>
                    <p class="text-truncate">
                      {{ searchGroups[4 * (row - 1) + (col - 1)].gname }}
                    </p>
                  </v-col>
                </v-row>
              </v-col>
            </template>
          </v-row>
        </v-col>

        <!-- 그룹 생성 모달 -->
      </v-row>
      <for-null v-else :myHeight="'591px'" />

      <v-dialog v-model="createGroupModal" persistent>
        <v-card>
          <v-card-title>
            <span>그룹 생성</span>
            <v-spacer></v-spacer>
            <v-btn icon @click="createGroupModal = false">
              <v-icon>mdi-close</v-icon>
            </v-btn>
          </v-card-title>
          <v-card-text class="pt-4 pb-0">
            <v-row no-gutters>
              <v-text-field
                v-model="groupName"
                label="그룹명"
                outlined
                clearable
                required
              ></v-text-field>
            </v-row>
            <v-row class="mt-0" no-gutters>
              <v-textarea
                v-model="groupDesc"
                rows="3"
                label="그룹 소개글"
                outlined
                no-resize
                clearable
              ></v-textarea>
            </v-row>
            <v-row class="mt-0" no-gutters>
              <v-select
                v-model="categorySelect"
                label="카테고리"
                :items="categoryList"
                item-text="cname"
                item-value="cno"
                outlined
              >
              </v-select>
            </v-row>
            <v-row class="mt-0" no-gutters>
              <v-col>
                <p class="subtitle-1">그룹 공개 범위</p>
                <v-radio-group v-model="scope">
                  <v-radio label="친구만" color="primary" value="1"></v-radio>
                  <v-radio
                    label="친구의 친구까지"
                    color="success"
                    value="2"
                  ></v-radio>
                  <v-radio label="비공개" color="warning" value="0"></v-radio>
                </v-radio-group>
              </v-col>
            </v-row>
          </v-card-text>
          <v-card-actions class="px-6">
            <v-row class="pt-0 pb-4" justify="end" no-gutters>
              <v-btn
                color="success"
                :disabled="!createGroupValid"
                @click="createGroup"
                >그룹 생성하기</v-btn
              >
            </v-row>
          </v-card-actions>
        </v-card>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script>
import axios from 'axios';
import ForNull from '@/components/user/profile/ForNull.vue';

const storage = window.sessionStorage;

export default {
  components: {
    ForNull,
  },
  props: ['uno', 'email', 'groupList', 'categoryList', 'friendList'],
  data() {
    return {
      groups: this.groupList,
      categories: this.categoryList,
      friends: this.friendList,
      groupListRow: 0,
      search: null,
      searchGroups: [],
      searchGroupListRow: 0,
      createGroupModal: false,
      groupName: null,
      groupDesc: null,
      categorySelect: null,
      scope: null,
      createGroupValid: false,
      groupNameValid: false,
      groupDescValid: false,
      categorySelectValid: false,
      scopeValid: false,
      icons: [
        'mdi-controller-classic',
        'mdi-book-open-page-variant',
        'mdi-soccer',
        'mdi-music-note',
        'mdi-hand-shake',
        'mdi-food-turkey',
        'mdi-face-woman-shimmer',
      ],
    };
  },
  created() {
    this.groupListRow = parseInt(this.groups.length / 4) + 1;
  },
  methods: {
    moveGroup(index, isSearch) {
      let params = new URLSearchParams();
      params.append('email', storage.getItem('user-email'));

      if (isSearch) {
        this.$store.commit('setGno', this.searchGroups[index].gno);
        params.append('gno', this.searchGroups[index].gno);
      } else {
        this.$store.commit('setGno', this.groups[index].gno);
        params.append('gno', this.groups[index].gno);
      }

      axios
        .post('isGroupMember', params)
        .then((response) => {
          if (!response.data.isExist) {
            alert('삭제된 그룹입니다.');
            this.$router.push('/');
            return;
          }
          this.memberStatus = response.data.memberStatus;
          this.$store.commit('setMemberStatus', this.memberStatus);
          this.$router.push('/group');
        })
        .catch((error) => {
          console.log(error);
        });
    },
    searchInput() {
      this.searchGroups = [];

      if (this.search != null && this.search != '') {
        for (let i = 0; i < this.groups.length; i++) {
          if (this.groups[i].gname.includes(this.search))
            this.searchGroups.push(this.groups[i]);
        }

        this.searchGroupListRow = parseInt(this.searchGroups.length / 4) + 1;
      }
    },
    createGroup() {
      console.log('pressed');
      axios
        .post('createGroup', {
          uno: this.uno,
          gname: this.groupName,
          gdesc: this.groupDesc,
          gcategory: this.categorySelect,
          gboundary: this.scope,
        })
        .then((response) => {
          if (response.data['is-success']) {
            axios
              .post('getGroupList', { email: this.email })
              .then((response) => {
                if (response.data['is-success']) {
                  this.groups = response.data.groupList;

                  for (let i = 0; i < this.groups.length; i++) {
                    this.groups[i].members = this.groups[i].guserList
                      .trim()
                      .split(' ');
                  }

                  this.createGroupModal = false;
                  this.groupListRow = parseInt(this.groups.length / 4) + 1;
                  alert(this.groupName + ' 그룹이 생성되었습니다.');
                }
              })
              .catch((error) => {
                console.log(error);
              });
          } else alert('그룹을 생성하는데 오류가 발생하였습니다.');
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  watch: {
    createGroupModal(createGroupModal) {
      if (!createGroupModal) {
        this.groupName = null;
        this.groupDesc = null;
        this.categorySelect = null;
        this.scope = null;
        this.groupNameValid = false;
        this.groupDescValid = false;
        this.categorySelectValid = false;
        this.scopeValid = false;
      }
    },
    groupName(groupName) {
      if (groupName != null && groupName != '') {
        this.groupNameValid = true;
        if (this.groupDescValid && this.categorySelectValid && this.scopeValid)
          this.createGroupValid = true;
      } else {
        this.groupNameValid = false;
        this.createGroupValid = false;
      }
    },
    groupDesc(groupDesc) {
      if (groupDesc != null && groupDesc != '') {
        this.groupDescValid = true;
        if (this.groupNameValid && this.categorySelectValid && this.scopeValid)
          this.createGroupValid = true;
      } else {
        this.groupDescValid = false;
        this.createGroupValid = false;
      }
    },
    categorySelect(categorySelect) {
      if (categorySelect != null && categorySelect != '') {
        this.categorySelectValid = true;
        if (this.groupNameValid && this.groupDescValid && this.scopeValid)
          this.createGroupValid = true;
      } else {
        this.categorySelectValid = false;
        this.createGroupValid = false;
      }
    },
    scope(scope) {
      if (scope != null && scope != '') {
        this.scopeValid = true;
        if (
          this.groupNameValid &&
          this.groupDescValid &&
          this.categorySelectValid
        )
          this.createGroupValid = true;
      } else {
        this.scopeValid = false;
        this.createGroupValid = false;
      }
    },
  },
};
</script>

<style></style>
