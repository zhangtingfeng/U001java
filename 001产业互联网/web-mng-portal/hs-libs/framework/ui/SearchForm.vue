<template>
  <form class="form-search" @submit.prevent="action(btns[0].name)">
    <div class="row">
      <template v-for="(f) in searchFields">
        <div v-if="!f.multi && f.name" v-bind:key="f.name" class="form-group col-md-2">
          <label class="control-label">{{$t(`${data.meta.name2}.field.${f.name}`)}}</label>
          <component
            form="search"
            v-if="f.editor"
            :is="getEditor(f.editor)"
            :args="f.editorArgs"
            :name="f.name"
            v-model.trim="data.search[f.name]"
          />
          <input v-else :name="f.name" class="form-control" v-model.trim="data.search[f.name]" />
        </div>
        <div v-else-if="f.multi && f.name" v-bind:key="f.name" class="form-group col-md-4">
          <label class="control-label">{{$t(`${data.meta.name2}.field.${f.name}`)}}</label>
          <div class="multi-controls">
            <component
              form="search"
              class="multi-control"
              v-if="f.editor"
              :is="getEditor(f.editor)"
              :args="f.editorArgs"
              :name="`${f.name}_1`"
              v-model.trim="data.search[f.name+`_1`]"
            />
            <input
              v-else
              :name="`${f.name}_1`"
              class="form-control multi-control"
              v-model.trim="data.search[f.name+`_1`]"
            />
            <label>~</label>
            <component
              form="search"
              class="multi-control"
              v-if="f.editor"
              :is="getEditor(f.editor)"
              :args="f.editorArgs"
              :name="`${f.name}_2`"
              v-model.trim="data.search[f.name+`_2`]"
            />
            <input
              v-else
              :name="`${f.name}_2`"
              class="form-control multi-control"
              v-model.trim="data.search[f.name+`_2`]"
            />
          </div>
        </div>
        <div v-else v-bind:key="f.id" class="form-group col-md-2">
          <label class="control-label">&nbsp;</label>
          <div>
            <a class="btn btn-link" href="#" @click.prevent="toggelMore" v-html="$t(f.id)"></a>
          </div>
        </div>
      </template>
      <div class="col-md-4" v-if="!btnNewLine">
        <label class="control-label">&nbsp;</label>
        <div class="input-group-btn">
          <button
            v-for="(btn) in data.searchBtns"
            v-bind:key="btn.name"
            :class="`btn btn-${btn.clz}`"
            :disabled="btn.disabled"
            @click.prevent="action(btn.name)"
          >
            <i :class="`fa fa-${btn.icon}`" v-if="btn.icon"></i>
            {{$t(btn.name)}}
          </button>
        </div>
      </div>
    </div>
    <div class="row" v-if="btnNewLine">
      <div class="btn-panel">
        <button
          v-for="(btn) in data.searchBtns"
          v-bind:key="btn.name"
          :class="`btn btn-${btn.clz}`"
          :disabled="btn.disabled"
          @click.prevent="action(btn.name)"
        >
          <i :class="`fa fa-${btn.icon}`" v-if="btn.icon"></i>
          {{$t(btn.name)}}
        </button>
      </div>
    </div>
  </form>
</template>

<script>
const SHOWMORE_ROW = 2;
const ROW_COUNT = 6;

import meta from "../meta";
import editors from "../editors";
import msg from "@c/msg";
import { auths } from "../user";

export default {
  data() {
    return { searchFields: [], btnNewLine: false, showMore: false };
  },
  props: ["data"],
  methods: {
    getEditor(name) {
      return editors.get(name);
    },
    layout() {
      let searchFields = [];
      let count = 0;
      for (let f of this.data.meta.fields) {
        if (!f.query) continue;
        searchFields.push(Object.assign({}, f, { multi: f.query > 99 }));
        count++;
        if (f.query > 99) count++;
      }

      let forceBtnNewLine = false;
      if (searchFields.length > SHOWMORE_ROW * ROW_COUNT) {
        forceBtnNewLine = true;
        if (this.showMore) {
          searchFields.push({ id: "hide-more" });
        } else {
          let length = 0;
          count = 0;
          let total = SHOWMORE_ROW * ROW_COUNT - 1;
          for (let f of searchFields) {
            let l1 = f.multi ? 2 : 1;
            if (length + l1 > total) break;
            length += l1;
            count++;
          }
          searchFields.length = count;
          searchFields.push({ id: "show-more" });
        }
      }

      // 按钮始终强迫换行
      // forceBtnNewLine = true;

      let btns = this.createBtns();
      // 计算按钮占用宽度
      let t = Math.ceil(btns.length / 2);
      let t1 = count % ROW_COUNT;
      this.btnNewLine = forceBtnNewLine || 0 == t1 || t + t1 > ROW_COUNT;

      this.data.searchBtns = btns;
      this.searchFields = searchFields;
    },
    createBtns() {
      let btns = [];
      let disabled = true;
      if (this.data.rowState.selected) {
        for (let s of this.data.rowState.selected) if (s) disabled = false;
      }
      if (this.data.searchActions) {
        for (let i = 0; i < this.data.searchActions.length; i++) {
          let a = this.data.searchActions[i];
          let b = {};
          if (typeof a == "string") {
            b = { name: a, icon: a };
            if (i == 0) b.clz = "primary";
          } else b = { name: a.name, icon: a.icon, clz: a.clz };
          if (!a.clz) b.clz = "default";

          if (a.disabled && disabled) b.disabled = true;

          if (!a.auth || auths.has(a.auth)) btns.push(b);
        }
      }
      return btns;
    },
    toggelMore() {
      this.showMore = !this.showMore;
      this.layout();
    },
    action(a) {
      let n = "search" + a.substr(0, 1).toUpperCase() + a.substr(1);
      if (this.data[n]) this.data[n]();
      else msg.info("notimpelement", "error");
    }
  },
  watch: {
    "data.rowState.selected"() {
      this.data.searchBtns = this.createBtns();
    }
  },
  mounted() {
    this.layout();
  }
};
</script>
<style scoped lang="scss">
.btn-panel {
  margin: auto -5px {
    bottom: -10px;
  }
  padding: 10px 5px;
  border-top: 1px solid #ddd;
  background-color: #fff;

  button {
    margin: auto 5px;
  }
}
.multi-controls {
  display: flex;
  flex-flow: row nowrap;
  > .multi-control {
    flex: 1;
  }
  > label {
    flex: 0;
  }
}
</style>
<i18n-yaml>
en:
  "show-more": "<i class='fa fa-angle-double-down'></i> Expand search"
  "hide-more": "<i class='fa fa-angle-double-up'></i> Collapse search"

cn:
  "show-more": "<i class='fa fa-angle-double-down'></i> 展开查询条件"
  "hide-more": "<i class='fa fa-angle-double-up'></i> 收缩查询条件"

</i18n-yaml>