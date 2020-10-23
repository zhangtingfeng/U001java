<template>
    <div class="img-upload">
        <label>{{title}}</label>
        <div class="file-loading">
            <input :id="id" name="file" type="file" accept="image/*" multiple>
        </div>
    </div>
</template>

<script>
    import "@/common/bootstrap-fileinput/css/fileinput.min.css";
    import "@/common/bootstrap-fileinput/js/fileinput.min.js";
    import "@/common/bootstrap-fileinput/js/zh.js";

    export default {
        data() {
            return {
                images: this.value
            }
        },
        props: ["id", "title", "value"],
        methods: {
            // Bootstrap FileInput
            uploadInit() {
                var _this = this;
                // 上传商品照片
                $("#" + _this.id).fileinput({
                    language: 'zh',                                          // 多语言设置，需要引入local中相应的js，例如locales/zh.js
                    theme: "explorer-fa",                               // 主题
                    uploadUrl: "api/order/upload/listing-aftermaket-imgs",         // 上传地址
                    minFileCount: 1,                                        // 最小上传数量
                    maxFileCount: 5,                                        // 最大上传数量
                    maxFileSize: 1024 * 5,           //单位为kb，如果为0表示不限制文件大小
                    overwriteInitial: false,                        // 覆盖初始预览内容和标题设置
                    showCancel: true,                                       // 显示取消按钮
                    showZoom: true,                                         // 显示预览按钮
                    showCaption: false,                                  // 显示文件文本框
                    dropZoneEnabled: true,                          // 是否可拖拽
                    uploadLabel: "上传照片",                         // 上传按钮内容
                    browseLabel: '<a class="btn btn-primary hn-btn">选择照片</a>', // 浏览按钮内容
                    showRemove: true,                                       // 显示移除按钮
                    browseClass: "layui-btn btn-file-browse",                        // 浏览按钮样式
                    uploadClass: "layui-btn",                        // 上传按钮样式
                    uploadExtraData: {},   // 上传数据
                    hideThumbnailContent: true,                  // 是否隐藏文件内容
                    // allowedFileExtensions: ['gif','jpg','png','bmp','jpeg'],
                    allowedFileTypes: ['image'],                  // 接收的文件后缀，如['jpg', 'gif', 'png'],不填将不限制上传文件后缀类型
                    fileActionSettings: {                               // 在预览窗口中为新选择的文件缩略图设置文件操作的对象配置
                        showRemove: true,                                   // 显示删除按钮
                        showUpload: true,                                   // 显示上传按钮
                        showDownload: false,                            // 显示下载按钮
                        showZoom: true,                                    // 显示预览按钮
                        showDrag: false,                                        // 显示拖拽
                        removeIcon: '<i class="fa fa-trash"></i>',   // 删除图标
                        uploadIcon: '<i class="fa fa-upload"></i>',     // 上传图标
                        uploadRetryIcon: '<i class="fa fa-repeat"></i>'  // 重试图标
                    },

                    initialPreview: [                                                                   //初始预览内容
                    ],
                    initialPreviewConfig: [                                                     // 初始预览配置 caption 标题，size文件大小 ，url 删除地址，key删除时会传这个
                    ],
                    layoutTemplates: {
                        close: '<a class="btn btn-lg close fileinput-remove" style="bottom:0;top:auto;right:-2px;"><i class="fa fa-trash" style="font-size:120%;"></i></a>'
                    }
                }).on('filebatchselected', function (event, data, id, index) {
                    $(this).fileinput("upload");
                });

                // 异步上传成功结果处理
                $("#" + _this.id).on("fileuploaded", function (e, data) {
                    var img = {"name": data.response.name, "id": data.response.id};
                    _this.images.push(img);
                });
                // 异步批量上传成功结果处理
                $("#" + _this.id).on("filebatchuploadsuccess", function (e, data) {
                });
                // 异步删除成功结果处理
                $("#" + _this.id).on("filedelete", function (e, data) {
                });
                $("#" + _this.id).on("filesuccessremove", function (event, data) {
                    var images = _this.images;
                    for (var i = 0; i < images.length; i++) {
                        if (data.indexOf(images[i].name) != -1) {
                            _this.images.splice(i, 1);
                        }
                    }
                });
                // 点击浏览框右上角X 清空文件后响应事件
                $("#" + _this.id).on("filecleared", function (e, data) {
                    _this.images = [];
                });
                // 上传成功回调
                $("#" + _this.id).on("filebatchuploadcomplete", function (e, data) {
                });
                // 上传失败回调
                $("#" + _this.id).on('fileerror', function (event, data, msg) {
                    tokenTimeOut(data);
                });

            },
        },
        mounted() {
            this.uploadInit();
        }
    }
</script>

<style lang="less">
    .file-input {
        position: relative;

        .btn-file-browse {
            position: absolute;
            top: -40px;
            right: 40px;

            i {
                display: none;
            }
        }
    }

    .file-drop-zone {
        border: none;
    }

    .hn-btn {
        color: #6abf4b;
        background-color: white;
    }

    .img-upload label {
        margin: 0 0 20px 0;
    }
</style>
