<template>
  <table class="table table-striped table-hover table-condensed">
    <thead>
      <tr class="bg-gray">
        <th width="30px" v-if="hasSelect">
          <checkbox v-model="all" />
        </th>
        <template v-for="(col) of meta.fields">
          <th
            v-bind:key="col.name"
            v-if="col.form != 0"
            :class="`col-width-${col.width}`"
          >{{$t(`${meta.name2}.field.${col.name}`)}}</th>
        </template>
      </tr>
    </thead>
    <tbody>
      <template v-for="(d, idx) in datas">
        <tr v-bind:key="idx" v-if="d.$op != -1">
          <td v-if="hasSelect">
            <checkbox v-model="d.$selected" />
          </td>
          <template v-for="(f) of meta.fields">
            <td v-bind:key="f.name" v-if="f.form != 0">
              <template v-if="f.form == 2">
                <component
                  v-if="f.view"
                  :is="getView(f.view)"
                  :args="f.viewArgs"
                  :name="f.name"
                  v-model="d[f.name]"
                />
                <template v-if="!f.view">{{d[f.name]}}</template>
              </template>
              <template v-if="f.form == 1">
                <input
                  v-if="!f.editor"
                  :name="f.name"
                  class="form-control"
                  v-model="d[f.name]"
                  v-validate="f.validate ? f.validate : ''"
                />
                <component
                  v-else
                  :is="getEditor(f.editor)"
                  :args="f.editorArgs"
                  :name="f.name"
                  v-model="d[f.name]"
                  :validate="f.validate ? f.validate : ''"
                />
              </template>
            </td>
          </template>
        </tr>
      </template>
    </tbody>
  </table>
</template>

<script>
import Vue from "vue";
import views from "../views";
import editors from "../editors";
import Checkbox from "@c/ui/Checkbox";
let components = $.extend({ Checkbox });

export default {
  components,
  props: ["meta", "datas", "select"],
  data() {
    return { all: false, hasSelect: this.select };
  },
  methods: {
    getEditor(name) {
      return editors.get(name);
    },
    getView(name) {
      return views.get(name);
    }
  },
  watch: {
    all() {
      if (this.datas) {
        for (var i = 0; i < this.datas.length; i++) {
          this.datas[i].$selected = this.all;
        }
      }
    }
  }
};
</script>

<style scoped>
.table {
  margin-bottom: 0;
}
.checkbox {
  margin-top: 0 !important;
  margin-bottom: 0 !important;
}
</style>
