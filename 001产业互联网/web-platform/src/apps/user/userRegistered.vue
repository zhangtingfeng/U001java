<template>
  <div>
    <table1 name="platform/userRegistered" :extend="userRegisteredData"></table1>
  </div>
</template>

<script>
import { Table1,Checkbox,dict,comps } from "@f/framework";
import { progress, msg, ajax } from "@f/vendor";
import Vue from "Vue";

export default {
  components: { Table1 },

  data() {
    return {
      userRegisteredData: {
        // 查询按钮
        searchActions: [
          {
            name: "query",
            clz: "primary",
            icon: "search"
          },
          {
            name: "exp",
            icon: "th"
          },
          {
            name: "del",
            icon: "th",
            disabled: true
          },

        ],
        listActions: null,
      }
    };
  }
};
</script>

<style scoped>
</style>
<i18n-yaml>
en:
  lock: "lock"
  unlock: "unlock"
cn:
  lock: "锁定"
  unlock: "解锁"
</i18n-yaml>
