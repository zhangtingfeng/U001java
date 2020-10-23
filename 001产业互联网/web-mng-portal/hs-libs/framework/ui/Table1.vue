<template>
  <div class="box">
    <div class="alert alert-danger alert-dismissible" v-if="data.state == -1">
      <h4 class>
        <i class="icon fa fa-warning"></i>
        {{$t("error.title")}}
      </h4>
      {{$t("error.msg")}}
    </div>
    <div class="box-header with-border bg-gray" v-if="data.state &gt; 0">
      <h4 class="box-title" style="font-size:15px;">{{$t(`${data.meta.name2}.title`)}}</h4>
      <div class="box-tools pull-right" v-if="data.state == 1">
        <div class="btn-toolbar">
          <div class="btn-group" v-if="!showSearchForm">
            <button
              v-for="(btn) in data.searchBtns"
              v-bind:key="btn.name"
              :class="`btn btn-search-action btn-${btn.clz}`"
              :title="$t(btn.name)"
              @click.prevent="searchAction(btn.name)"
            >
              <i :class="`fa fa-${btn.icon}`" v-if="btn.icon"></i>
            </button>
          </div>
          <button
            type="button"
            class="btn btn-search-action"
            @click.prevent="showSearchForm = !showSearchForm"
          >
            <i class="fa fa-chevron-up" v-if="showSearchForm" />
            <i class="fa fa-chevron-down" v-else />
          </button>
        </div>
      </div>
      <div class="box-tools pull-right" v-if="data.state == 2">
        <button class="btn btn-box-tool" @click.prevent="data.formCancel()" :title="$t('cancel')">
          <i class="fa fa-chevron-left" />
        </button>
      </div>
    </div>
    <template v-if="data.state == 1">
      <div class="box-header with-border" v-if="showSearchForm">
        <search-form :data="data" />
      </div>
      <div class="box-body table-responsive">
        <page-table :data="data" />
      </div>
      <div class="box-footer clearfix" v-if="data.data.data">
        <pager :data="data" />
      </div>
    </template>
    <template v-if="data.state == 2">
      <div class="box-body">
        <meta-form :data="data">
          <slot slot="extend" name="extend-inner"></slot>
        </meta-form>
      </div>
      <slot name="extend" :data="data"></slot>
    </template>
  </div>
</template>

<script>
import meta from "../meta/meta";
import Data from "../meta";
import DataSearch from "../meta/search";
import DataExport from "../meta/export";
import DataForm from "../meta/form";

import SearchForm from "../ui/SearchForm";
import PageTable from "../ui/PageTable";
import Pager from "../ui/Pager";
import MetaForm from "../ui/Form";

function EmptyData() {}

function Actions(actions, data, prefix) {
  let rs = {};
  for (let a of actions) {
    let n = a;
    if (prefix) {
      n = prefix + n.substr(0, 1).toUpperCase() + n.substr(1);
    }
    rs[a] = () => {
      data[n]();
    };
  }
  return rs;
}

export default {
  components: {
    SearchForm,
    PageTable,
    Pager,
    MetaForm
  },
  data() {
    return {
      meta: {},
      showSearchForm: true,
      data: { state: 0, searchBtns: [] }
    };
  },
  props: ["name", "extend"],
  watch: {
    "data.state"() {
      setTimeout(() => {
        $(window).trigger("resize");
      }, 100);
    }
  },
  methods: {
    createData() {
      return Data.init(
        this.meta,
        DataSearch,
        DataForm,
        DataExport,
        this.extend || EmptyData
      );
    },
    searchAction(a) {
      let n = "search" + a.substr(0, 1).toUpperCase() + a.substr(1);
      if (this.data[n]) this.data[n]();
      else msg.info("notimpelement", "error");
    }
  },
  mounted() {
    meta
      .get(this.name)
      .then(data => {
        debugger;
        if (data.error) throw "1";
        this.meta = data;
        let msgs = meta.msgs(this.meta.name2);
        this.$i18n.mergeLocaleMessage("en", msgs.en);
        this.$i18n.mergeLocaleMessage("cn", msgs.cn);
        this.data = this.createData();
        this.data.state = 1;
        // if (window.location.href.split("?").length > 1) {
          this.data.searchQuery();
        // }
      })
      .catch(() => {
        this.data.state = -1;
      });
  }
};
</script>

<style lang="less" scoped>
.btn-search-action {
  padding: 5px;
  font-size: 12px;
}
</style>
