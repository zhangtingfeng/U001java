<template>
  <form @submit.prevent="action()" :class="`form-meta form-${data.meta.name}`">
    <div class="row">
      <template v-for="(f) in data.meta.fields">
        <template v-if="f.form">
          <div v-bind:key="f.name + '-group'" v-if="f.group" class="col-md-12 form-group-title">
            <h4 class="bg-gray">{{$t(`${data.meta.name2}.group.${f.name}`)}}</h4>
          </div>
          <div v-bind:key="f.name" :class="`form-group col-md-${(f.width ? f.width : 1) * 3} ${errors.has(f.name) ? 'has-error' : ''}`">
            <label class="control-label" v-if="f.validate.indexOf('required')>=0">
              {{$t(`${data.meta.name2}.field.${f.name}`)}}（*）
            </label>
            <label class="control-label" v-else>
              {{$t(`${data.meta.name2}.field.${f.name}`)}}
              <span class="error" v-if="errors.has(f.name)">({{errors.first(f.name)}})</span>
            </label>
            <input v-if="!f.editor" :name="f.name" class="form-control" v-model="data.form[f.name]" v-validate="f.validate ? f.validate : ''" :readonly="f.form == 3 || (f.form == 2 && data.form.$op != 1)" :data-vv-name="f.name" />
            <component v-if="f.editor" :is="getEditor(f.editor)" :args="f.editorArgs" :name="f.name" v-model="data.form[f.name]" v-validate="f.validate ? f.validate : ''" :readonly="(f.form == 3) || (f.form == 2 && data.form.$op != 1)" :data-vv-name="f.name" />
          </div>
          <div v-bind:key="`${f.name}-c`" class="clearfix" v-if="lineBreaks[f.name]"></div>
        </template>
      </template>
    </div>
    <slot name="extend" :data="data"></slot>
    <div class="actions">
      <template v-for="(btn) in btns">
        <button href="#" v-if="btn.show != false" v-bind:key="btn.name" :class="`btn btn-${btn.clz}`" :disabled="btn.disabled" @click.prevent="action(btn)">
          <i :class="`fa fa-${btn.icon}`" v-if="btn.icon"></i>
          {{$t(btn.name)}}
        </button>
      </template>
    </div>
  </form>
</template>

<script>
import { msg } from "@f/vendor";
import { comps, editors, auths } from "@f/framework";
import Upload from "./upload";

export default {
  components: { Upload: Upload },
  data() {
    return { lineBreaks: this.layout() };
  },
  computed: {
    btns() {
      let btns = [];
      if (this.data.formActions) {
        for (let i = 0; i < this.data.formActions.length; i++) {
          let a = this.data.formActions[i];
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
  props: ["data"],
  methods: {
    getEditor(name) {
      if (name == "upload") {
        return "Upload";
      }
      return editors.get(name);
    },
    layout() {
      let lineBreaks = {},
        count = 0,
        lastField;
      for (let i in this.data.meta.fields) {
        let f = this.data.meta.fields[i];
        if (0 == f.form) continue;
        count += f.width || 1;
        if (f.group) {
          if (lastField) lineBreaks[lastField.name] = true;
          count = f.width || 1;
          lastField = f;
        } else if (count > 4) {
          if (lastField) {
            lineBreaks[lastField.name] = true;
            count = f.width || 1;
            lastField = f;
          } else {
            lineBreaks[f.name] = true;
            count = 0;
            lastField = null;
          }
        } else if (f.break) {
          lineBreaks[f.name] = true;
          count = 0;
          lastField = null;
        } else if (f.lineBreaks) {
          if (lastField) lineBreaks[lastField.name] = true;
          count = f.width || 1;
          lastField = f;
        } else {
          lastField = f;
        }
      }
      return lineBreaks;
    },
    action(btn) {
      if (!btn) {
        for (let b of this.btns)
          if ("primary" == b.clz) {
            btn = b;
            break;
          }
      }
      if (!btn) {
        btn = this.btns[0];
      }

      if (btn.action) {
        btn.action.apply(this.data, [btn]);
      } else {
        let name = btn.name;
        let n = "form" + name.substr(0, 1).toUpperCase() + name.substr(1);
        if (this.data[n]) this.data[n]();
        else msg.info("notimpelement", "error");
      }
    }
  },
  mounted() {
    this.data.$v = this.$validator;
  }
};
</script>
<style scoped>
.actions {
  border-top: 1px solid #eee;
}

.actions .btn {
  margin: 1em 1em 1em 0;
}
.actions .btn i {
  margin-right: 0.5em;
}
</style>
