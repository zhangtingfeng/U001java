<template>
  <table class="box-body table table-striped">
    <thead>
      <th width="45%">名称</th>
      <th width="45%">说明</th>
      <th width="10%">
        <a href="#" class="btn btn-link" @click.prevent="doAdd">
          <i class="fa fa-plus"></i>
        </a>
      </th>
    </thead>
    <tbody>
      <tr v-for="(p, i) of props" :key="i">
        <td>
          <input class="form-control input-sm" v-model="p.id" />
        </td>
        <td>
          <input class="form-control input-sm" v-model="p.name" />
        </td>
        <td>
          <a href="#" class="btn btn-link" @click.prevent="doRemove(i)">
            <i class="fa fa-times"></i>
          </a>
        </td>
      </tr>
    </tbody>
  </table>
</template>

<script>
export default {
  props: ["props"],
  data() {
    return {};
  },
  methods: {
    doAdd() {
      this.props.push({ id: "", name: "" });
    },
    doRemove(i) {
      this.props.splice(i, 1);
    },
  },
};
</script>
