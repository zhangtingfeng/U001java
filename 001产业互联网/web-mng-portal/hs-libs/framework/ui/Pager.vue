<template>
  <div class="row">
    <div class="col-md-6">
      <select name="pagesize" v-model="pageSize" class="form-control form-control pageSizeClass">
        <option value="20">20</option>
        <option value="50">50</option>
        <option value="100">100</option>
      </select>
      <p class="showPageInfo">{{$t("desc", desc)}}</p>
      <div class="clear"></div>
    </div>
    <nav class="col-md-6" aria-label="label">
      <ul class="pagination pagination-sm no-margin pull-right">
        <li :class="{disabled: data.data.idx &lt; 2}">
          <a href="#" aria-label="First" @click.prevent="go(1)">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <li v-for="(p, i) of pages" v-bind:key="i" :class="{active:p == data.data.idx}">
          <a href="#" @click.prevent="go(p)">
            <span aria-hidden="true">{{p}}</span>
          </a>
        </li>
        <li :class="{disabled: data.idx &gt;= data.pageCount}">
          <a href="#" aria-label="Last" @click.prevent="go(data.data.pageCount)">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script>
import msg from "@c/msg";

export default {
  data() {
    return { pageSize: 20 };
  },
  props: ["data"],
  computed: {
    pages() {
      let pages = [];
      if (this.data.data) {
        for (
          let i = this.data.data.idx;
          i > 0 && i > this.data.data.idx - 6;
          i--
        )
          pages.push(i);
        pages.reverse();
        for (
          let i = this.data.data.idx + 1;
          i <= this.data.data.pageCount && i < this.data.data.idx + 6;
          i++
        )
          pages.push(i);
        return pages;
      }
    },
    desc() {
      if (this.data && this.data.data && this.data.data.data) {
        let from = (this.data.data.idx - 1) * this.data.data.size + 1;
        let to = from + this.data.data.data.length - 1;
        return { from, to, total: this.data.data.total };
      }
      return { from: 0, to: 0, total: 0 };
    }
  },
  watch: {
    pageSize(val) {
      this.data.pageSize = val;
      this.data.searchQuery(1);
    }
  },
  methods: {
    go(p) {
      if (this.data.page == p) return;
      this.data.searchQuery(p);
    }
  }
};
</script>
<style>
.pageSizeClass {
  width: 80px;
  float: left;
}
.showPageInfo {
  margin: 10px;
  float: left;
}
.clear {
  clear: both;
}
</style>
<i18n-yaml>
en:
  desc: "From {from} to {to} / {total} total rows"
cn:
  desc: "从{from}到{to} / 共{total}条数据"
</i18n-yaml>
