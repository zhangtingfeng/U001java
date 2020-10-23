# 前端项目脚手架

本项目作为 hanshun 的前端项目脚手架,支持输出 chunk 由框架调用,下面对脚手架说明

## 目录结构

| 目录      | 说明                                                                                         |
| :-------- | :------------------------------------------------------------------------------------------- |
| `build`   | 系统构建脚本                                                                                 |
| `hs-libs` | framework 支持目录， 如果需要 framework 支持，需要将基础框架的 vendor,framework 复制到该目录 |
| `src`     | 系统源代码                                                                                   |
| `dist`    | 编译目标目录                                                                                 |
| `static`  | 系统静态资源                                                                                 |

## 指令说明

1. 清除所有的编译输出

```bash
npm run clean
```

2. 启动开发测试服务器

```bash
npm run dev
```

3. 编译 release 版本

```bash
npm run build
```

## 配置说明

系统配置文件`appsetting.js`,用来配置开发环境以及编译的选项. 系统配置说明如下:

```javascript
{
    name: "",    // 模块名称
    version: "", // 版本号，设置vendor和framework的版本号
    dev: {},     // 开发环境配置
    modules:{}   // 系统模块配置
}
```

### 编译选项

主要是提高编译时的一些开关选项.
| 配置项 | 说明 |
| :---- | :---- |
| `framework` | 是否使用系统的框架程序 |
| `ana` | 编译时输出模块依赖以及 chunk 包含说明, 会包含进来 `WebpackBundleAnalyzer` 插件 |
| `ie` | 增加 IE 支持 |
| `jquery` | 增加 jQuery 支持 |
| `vue` | 增加 VUE 支持 |
| `moment` | 支持 moment 的优化导入，避免导入多余语言定义文件 |

### 开发选项

| 配置项          | 说明                                                 |
| :-------------- | :--------------------------------------------------- |
| `port`          | dev 服务器端口                                       |
| `proxy`         | 后端 WebApi 的代理配置                               |
| `proxy->url`    | 需要代理的 url pattern                               |
| `proxy->target` | 代理的目标 url                                       |
| `proxy->path`   | `org`=`target` 目录映射, `org`目录映射到`target`目录 |
