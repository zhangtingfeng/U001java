<template>
  <section class="content">
    <div class="box">
      <div class="box-body table-responsive">
        <table
          class="table table-striped table-bordered table-hover table-condensed text-nowrap page-table"
        >
          <thead>
            <tr class="bg-gray">
              <th>#</th>
              <template v-for="c of cols">
                <th :key="c">Column {{c}}</th>
              </template>
            </tr>
          </thead>
          <tbody>
            <tr v-for="r of rows" :key="r">
              <td>
                <div class="btn-group">
                  <button data-toggle="dropdown" class="btn btn-default btn-xs dropdown-toggle">
                    <i class="fa fa-pencil"></i>
                    <i class="fa fa-caret-down"></i>
                  </button>
                  <ul class="dropdown-menu">
                    <li>
                      <a href="#" @click.prevent>Test 1</a>
                    </li>
                    <li>
                      <a href="#" @click.prevent>Test 2</a>
                    </li>
                  </ul>
                </div>
              </td>
              <template v-for="c of cols">
                <td :key="c">Data {{r}} - {{c}}</td>
              </template>
            </tr>
          </tbody>
        </table>
        <br />
        <br />
        <br />
        <br />
        <br />
      </div>
    </div>
  </section>
</template>

<script>
export default {
  data() {
    return { cols: 20, rows: 20 };
  },
};
</script>

<style lang="less" scoped>
.table {
  thead {
    tr {
      th {
        padding-top: 10px;
        padding-bottom: 10px;
      }
    }
  }
}
</style>
