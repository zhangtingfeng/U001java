<template>
  <table class="box-body table table-striped">
    <colgroup>
      <col width="100px" />
      <col />
      <col width="50" />
    </colgroup>
    <tbody>
      <tr v-for="p of props" :key="p.id">
        <td>{{ p.name }}</td>
        <template v-if="p.ext">
          <td>
            <component
              v-if="p.editor"
              :is="getEditor(p.editor)"
              :args="p.editorArgs"
              v-model="p.v1"
              @change="doChange2(p)"
              @input="doChange2(p)"
            />
            <input
              v-else
              class="form-control input-sm"
              v-model="p.v1"
              @change="doChange2(p)"
            />
          </td>
          <td>
            <input
              class="form-control input-sm"
              v-model="p.v2"
              @change="doChange2(p)"
            />
          </td>
        </template>
        <td v-else colspan="2">
          <component
            v-if="p.editor"
            :is="getEditor(p.editor)"
            :args="p.editorArgs"
            v-model="p.value"
            @change="doChange(p)"
            @input="doChange(p)"
          />
          <input
            v-else
            class="form-control input-sm"
            v-model="p.value"
            @change="doChange(p)"
          />
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
import { editors } from "@f/framework";

export default {
  props: ["props"],
  data() {
    return {};
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      for (let p of this.props) {
        this.procArgs(p);
      }
    },
    procArgs(p) {
      if (p.value) {
        let t = p.value.split(".");
        p.v1 = t[0];
        p.v2 = t[1];
      } else {
        p.v1 = "";
        p.v2 = "";
      }
    },
    getEditor(name) {
      return editors.get(name);
    },
    doChange(p) {
      this.$emit("change2", p);
    },
    doChange2(p) {
      p.value = `${p.v1}.${p.v2}`;
      this.doChange(p);
    },
  },
};
</script>
