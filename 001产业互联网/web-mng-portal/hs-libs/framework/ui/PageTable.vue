<template>
  <table
    class="table table-striped table-bordered table-hover table-condensed text-nowrap page-table"
    v-if="data && data.meta"
  >
    <thead>
      <tr class="bg-gray">
        <th width="30px">
          <checkbox v-model="all" />
        </th>
        <th
          v-if="menus && menus.length"
          :width="menus.length == 1 ? '60px' : '36px'"
        >{{ $t("action") }}</th>
        <template v-for="col in data.meta.fields">
          <th
            v-bind:key="col.name"
            v-if="col.list"
            :class="`col-width-${col.width}`"
          >{{ $t(`${data.meta.name2}.field.${col.name}`) }}</th>
        </template>
      </tr>
    </thead>
    <tbody v-if="data && data.data">
      <tr
        v-for="(d, i) in data.data.data"
        v-bind:key="i"
        :class="{ 'bg-gray': checked[i] }"
        @click="select(i)"
      >
        <td>
          <checkbox v-model="checked[i]" />
        </td>
        <td v-if="menus && menus.length">
          <div class="btn-group" v-if="menus.length == 1">
            <a
              v-for="(a, j) in menus"
              v-bind:key="j"
              href="#"
              @click.prevent.stop="action(a.name, i)"
              class="btn btn-xs btn-default"
            >
              <i v-if="a.icon" :class="`fa fa-${a.icon}`"></i>
              {{ $t(a.name) }}
            </a>
          </div>
          <div class="btn-group" v-if="menus.length &gt; 1">
            <button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">
              <i class="fa fa-pencil"></i>
              <i class="fa fa-caret-down"></i>
            </button>
            <ul class="dropdown-menu">
              <li v-for="(a, j) in menus" v-bind:key="j">
                <a href="#" @click.prevent="action(a.name, i)" :class="a.clz">
                  <i v-if="a.icon" :class="`fa fa-${a.icon}`"></i>
                  {{ $t(a.name) }}
                </a>
              </li>
            </ul>
          </div>
        </td>
        <template v-for="col in data.meta.fields">
          <td v-bind:key="col.name" v-if="col.list">
            <component
              v-if="col.view"
              :is="getView(col.view)"
              :args="col.viewArgs"
              :name="col.name"
              :row="d"
              :rowIdx="i"
              v-model="d[col.name]"
            />
            <template v-if="!col.view">{{ d[col.name] }}</template>
          </td>
        </template>
      </tr>
    </tbody>
  </table>
</template>

<script>
import Vue from "vue";
import ui from "@c/ui";
import msg from "@c/msg";
import views from "../views";
import { auths } from "../user";

let components = $.extend({}, { Checkbox: ui.Checkbox });

export default {
  components,
  data() {
    return { all: false, checked: this.data.rowState.selected };
  },
  computed: {
    menus() {
      let btns = [];
      if (this.data.listActions) {
        for (let i = 0; i < this.data.listActions.length; i++) {
          let a = this.data.listActions[i];
          if (typeof a == "string") {
            a = { name: a, icon: a };
            if (i == 0) a.clz = "primary";
          }
          if (!a.clz) a.clz = "default";
          if (!a.auth || auths.has(a.auth)) btns.push(a);
        }
      }
      return btns;
    }
  },
  props: {
    data: {}
  },
  methods: {
    getView(name) {
      return views.get(name);
    },
    select(i) {
      Vue.set(this.checked, i, !this.checked[i]);
    },
    action(a, i) {
      this.data.rowState.active = i;
      let n = "list" + a.substr(0, 1).toUpperCase() + a.substr(1);
      if (this.data[n]) this.data[n]();
      else msg.info("notimpelement", "error");
    }
  },
  watch: {
    all() {
      if (this.data && this.data.data && this.data.data.data) {
        for (var i = 0; i < this.data.data.data.length; i++) {
          this.checked[i] = this.all;
        }
      }
    },
    "data.data"() {
      this.checked = this.data.rowState.selected;
    }
  }
};
</script>
<i18n>
{
	"en": {
    "action": "Operate"
  },
	"cn": {
    "action": "操作"
	}
}
</i18n>