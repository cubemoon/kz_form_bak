define([
		'util/doT',
		'util/ajax',
		'text!template/form.html!strip'
	],
	function(doT, ajax, formTpl) {
		//var formTplFn;
		var formTplFn = doT.template(formTpl);
		var $formList = $('.f-form-page');
		var siteId;

		function initView() {

			$.getJSON(location.href + '?_=' + (new Date().getTime()), function(ret) {
				if (ret.data.id) {
					siteId = ret.data.site_id;
					$formList.html(formTplFn(ret.data));
					bindSelectEvent();
					bindUploadPicEvent();
					bindPostEvent();
					showPV(ret.data.id);
				} else {
					$formList.html('表单不存在或者被删除')
				}
			})
		}

		function bindSelectEvent() {
			var select = $('[data-name="id_dropdown"]');
			selectAction(select);
		}

		function selectAction(item, callBack) {
			//var optHide;
			item
				.on('click', '.f-select', function() {
					if ($(this).find('.f-option-list')[0].style.display == 'none') {
						$('.f-option-list').hide();
						$('.f-form-item').css({
							'z-index': 1
						});
						$(this).find('.f-option-list').show();

						$(this).closest('.f-form-item').css({
							'z-index': 10
						});
					} else {
						$(this).find('.f-option-list').hide();
						$(this).closest('.f-form-item').css({
							'z-index': 1
						});
					}

				})
				.on('click', '[data-action=option]', function() {
					var $emText = $(this).closest('.f-select').find('em');
					var $options = $(this).closest('.f-option-list');
					var thisVal = $(this).html();
					var lid = $(this).attr('data-lid');
					$emText.html(thisVal);
					if (lid) {
						$emText.attr('data-lid', lid);
					}
					if (callBack) {
						callBack();
					}
				})
		}

		function creatFormTitle(data, item) {
			data.id = $(item).attr('data-id');
			data.title = $(item).find('h2').find('em').text();
			data.instruct = "";
			if ($(item).find('h2').find('span')[0]) {
				data.required = true
			} else {
				data.required = false
			}
			return data;
		}

		function creatFormLabel(data, item, valueList) {
			var $valueList = $(item).find('label');
			$.each($valueList, function(index, item) {
				var value = {};
				value.lid = $(item).attr('data-lid')
				value.name = $(item).find('span').text();
				value.img = $(item).find('em img').attr('src');
				if ($(item).find('input')[0].checked) {
					value.selected = true;
				} else {
					value.selected = false;
				}
				valueList.push(value);
			})
			return valueList;
		}

		function creatFormData() {
			var formData = {};
			var fields = [];
			var valueList = [];
			//var $formTitle = $('h1', $formList);
			var $formDataItem = $('.f-form-item', $formList);
			$.each($formDataItem, function(index, item) {
				var data;
				var dataName = $(item).attr('data-name');
				var hasImg = false;
				if ($(item).find('img').length > 0) {
					hasImg = true
				}
				if (dataName == 'id_checkbox') {
					valueList = [];
					data = {};
					if (hasImg) {
						data.name = 'id_image_checkbox';
					} else {
						data.name = 'id_checkbox';
					}
					creatFormTitle(data, item);
					creatFormLabel(data, item, valueList);
					data.value = valueList;
				}
				if (dataName == 'id_radio') {
					valueList = [];
					data = {};

					if (hasImg) {
						data.name = 'id_image_radio';
					} else {
						data.name = 'id_radio';
					}
					creatFormTitle(data, item);
					creatFormLabel(data, item, valueList);
					data.value = valueList;
				}
				if (dataName == 'id_section') {
					data = {};
					data.name = 'id_section';
					data.id = index;
					data.title = $(item).find('h2').find('em').text();
					data.subtitle = "";
				}
				if (dataName == 'id_text') {
					data = {};
					data.name = 'id_text';
					data.value = $(item).find('.f-input').val();
					creatFormTitle(data, item);
				}
				if (dataName == 'id_dropdown') {
					valueList = [];
					data = {};
					data.name = 'id_dropdown';
					creatFormTitle(data, item);
					var $option = $(item).find('li');
					var optionList = [];
					$.each($option, function(index, item) {
						var option = {};
						option.lid = $(item).attr('data-lid');
						option.name = $(item).text();
						if ($(item).attr('data-lid') == $(item).closest('.f-select ').find('em').attr('data-lid')) {
							option.selected = true
						} else {
							option.selected = false
						}
						optionList.push(option)
					})
					data.value = optionList;
				}
				if (dataName == 'id_textarea') {
					data = {};
					data.name = 'id_textarea';
					data.value = $(item).find('textarea').val();
					creatFormTitle(data, item);
				}
				if (dataName == 'id_image') {
					data = {};
					data.name = 'id_image';
					creatFormTitle(data, item);
					data.description = $(item).find('.f-description').find('span').text();
					data.img =  $(item).find('img').attr('src');
					data.value = $(item).find('img').attr('src');
				}
				if (dataName == 'id_picture') {
					data = {};
					data.name = 'id_picture';
					data.id = index;
					data.url =  $(item).find('a').attr('href');
					data.descriptions =  $(item).find('span').text();
					data.img = $(item).find('img').attr('src');
				}
				fields.push(data);
			})


			formData.title = $formList.find('h1').text();
			formData.subtitle = $formList.find('.f-subtitle').text();
			formData.title_visible = true;
			formData.published = true;
			formData.fields = fields;
			formData.id = $formList.find('h1').attr('surveyId');
			var body = JSON.stringify(formData);
			return body;

			// var HTML_CHARS = {
			// 	'<': '&lt;',
			// 	'>': '&gt;',
			// }

			// return (body + '').replace(/[<>]/g, function($0) {
			// 	return HTML_CHARS[$0];
			// });
		}

		function checkForm() {
			var stutas = true;
			var $formDataItem = $('.f-form-item', $formList);
			$.each($formDataItem, function(index, item) {
				var required = $(item).attr('data-required');
				var name = $(item).attr('data-name');
				if (required == 'true') {
					if (name == 'id_text' || name == 'id_textarea') {
						var val = $(item).find('input').val();
						var textVal = $(item).find('textarea').val();
						if (val == '' || textVal == '') {
							alert('您有没填写的必填项');
							$(item)[0].scrollIntoView();
							$(item).find('input').focus();
							$(item).find('textarea').focus();
							stutas = false;
							return false;
						} else if ($(item).find('input').attr('type') == 'email') {
							var email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
							if (!email.test(val)) {
								$(item).find('input')[0].scrollIntoView();
								$(item).find('input').focus();
								alert('请输入正确的Email！');
								stutas = false;
								return false;
							}
						} else if ($(item).find('input').attr('type') == 'tel') {
							var tel = /^((\+?86)|(\(\+86\)))?1\d{10}$/;
							if (!tel.test(val)) {
								$(item).find('input')[0].scrollIntoView();
								$(item).find('input').focus();
								alert('请输入正确的手机号！');
								stutas = false;
								return false;
							}
						}
					} else if (name == 'id_checkbox' || name == 'id_image_checkbox' || name == 'id_radio' || name == 'id_image_radio') {
						var box = $(item).find('input');
						var num = 0;
						$.each(box, function(i, v) {
							if (v.checked) {
								num += 1;
							}
						})
						if (!num) {
							alert('您有没选择的必选项');
							$(item)[0].scrollIntoView();
							stutas = false;
							return false;
						}
					}else if(name == 'id_dropdown'){
						var selected = $(item).find('.f-select > em').attr('data-lid');
						if(!!!selected){
							alert('您有没选择的必选项');
							$(item)[0].scrollIntoView();
							stutas = false;
							return false;
						}
					}else if(name == 'id_image'){
						if($(item).find('img').attr('src') == 'http://kzcdn.itc.cn/res/skin/images/img/pic-one.png') {
							$(item).find('input').focus();
							alert("请选择合适的照片上传！");
							stutas = false;
							return false;
						}
					}
				} else {
					var val = $(item).find('input').val();
					if ($(item).find('input').attr('type') == 'email') {
						var email = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
						if (!email.test(val) && val) {
							$(item).find('input')[0].scrollIntoView();
							$(item).find('input').focus();
							alert('请输入正确的Email！');
							stutas = false;
							return false;
						}
					} else if ($(item).find('input').attr('type') == 'tel') {
						var tel = /^((\+?86)|(\(\+86\)))?1\d{10}$/;
						if (!tel.test(val) && val) {
							$(item).find('input')[0].scrollIntoView();
							$(item).find('input').focus();
							alert('请输入正确的手机号！');
							stutas = false;
							return false;
						}
					}
				}
				return;
			})
			return stutas;
		}

		function bindUploadPicEvent(){
			$formList.on('change', '[data-action="add-image"]', function() {
				uploadPicture(this);
			});
		}

		function uploadPicture(ipt) {
			var formdata = new FormData();
			formdata.append('image', ipt.files[0]);
			ajax.uploadPicture(siteId, formdata, function(ret) {
				var thisImg = {};
				thisImg.img = ret.data;
				$formList.find('.image-style').find('.f-image').attr('src',thisImg.img);
			}, function() {
				alert('文件超过5M或者格式有误，请重新选择图片！');
			}, {
				processData: false,
				contentType: false
			})
		}

		function bindPostEvent() {
			var $button = $('.f-btn-wrap');
			var surveyId = $formList.find('h1').attr('data-id');
			$button.on('click', '[data-action="post-form"]', function() {
				if (checkForm()) {
					var body = creatFormData();
					$.ajax({
						url: surveyId + '/answers',
						type: 'post',
						data: body,
						dataType: 'JSON',
						success: postCallback,
						error: postError
					});
				}
			})
		}

		function postCallback(ret) {
			alert('您已经成功提交，谢谢！')
			location.href = ret.data.value
		}

		function postError() {
			alert("提交失败！");
		}

		function showPV(id) {
			$.ajax('/forms/' + id + '/visits', {
				type: 'post'
			});
		}

		function show() {
			initView();
		}

		function preload() {
			return module;
		}

		var module = {
			preload: preload,
			show: show
		};

		return module;
	});