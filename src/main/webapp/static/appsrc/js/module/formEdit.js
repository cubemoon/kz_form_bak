define([
		'util/doT',
		'util/ajax',
		'util/select',
		'util/alert',
		'util/share',
		//'plugins/highcharts',
		'text!template/form_item/checkbox.html!strip',
		'text!template/form_item/input.html!strip',
		'text!template/form_item/line.html!strip',
		'text!template/form_item/radio.html!strip',
		'text!template/form_item/select.html!strip',
		'text!template/form_item/textarea.html!strip',
		'text!template/form_item/item_list.html!strip',
		'text!template/form_item/image.html!strip',
		'text!template/form_item/picture.html!strip',
		'text!template/form_setting/checkbox.html!strip',
		'text!template/form_setting/line.html!strip',
		'text!template/form_setting/select.html!strip',
		'text!template/form_setting/text.html!strip',
		'text!template/form_setting/select_item.html!strip',
		'text!template/form_setting/select_del_item.html!strip',
		'text!template/form_setting/search_page.html!strip',
		'text!template/form_setting/image.html!strip',
		'text!template/form_setting/picture.html!strip',
		'text!template/form_setting/picture_del_item.html!strip',
		'text!template/form_setting/picture_item.html!strip',
		'text!template/sidebar.html!strip',
		'text!template/form_edit.html!strip',
		'text!template/complate.html!strip',
		'text!template/form_answers.html!strip',
		'text!template/answers/answer.html!strip',
		'text!template/answers/answers.html!strip!',
		'text!template/answers/text.html!strip',
		'text!template/answers/share.html!strip',
		'text!template/table/table.html!strip',
		'text!template/pageNav.html!strip'
	],
	function(doT, ajax, select, Alert, Share, iCheckboxTpl, iInputTpl, iLineTpl, iRadioTpl, iSelectTpl, iTextareaTpl, itemListTpl, imageTpl,iPictureTpl,
			 sCheckboxTpl, sLineTpl, sSelectTpl, sTextTpl, selectItemTpl, selectDelItemTpl, searchPageTpl, sImageTpl, sPictureTpl, sPictureDelItemTpl, sPictureItemTpl, sideBarTpl,
			 formEditTpl, complateTpl, formAnswersTpl, answerTpl, answersTpl, textAnswerTpl, shareTpl, tableTpl, pageNavTpl) {
		var iCheckboxTplFn, iInputTplFn, iLineTplFn, iRadioTplFn, iSelectTplFn, iTextareaTplFn, itemListTplFn, imageTplFn, iPictureTplFn,
			sCheckboxTplFn, sLineTplFn, sSelectTplFn, sTextTplFn, selectItemTplFn, selectDelItemTplFn, searchPageTplFn, sImageTplFn, sPictureTplFn, sPictureDelItemTplFn, sPictureItemTplFn, sideBarTplFn,
			formAnswersTplFn, complateTplFn, formEditTplFn, answerTplFn, answersTplFn, shareTplFn, textAnswerTplFn, tableTplFn, pageNavTplFn;
		var isSave = true;
		var $sideBar = $('.f-side');
		var $formCon = $('.f-con');

		var $button, $setting, $formList, $formBox;//, $sideBar, $formCon;
		var siteId = $('.f-con').attr('siteId');



		function initView() {
			iCheckboxTplFn = doT.template(iCheckboxTpl);
			iInputTplFn = doT.template(iInputTpl);
			iLineTplFn = doT.template(iLineTpl);
			iRadioTplFn = doT.template(iRadioTpl);
			iSelectTplFn = doT.template(iSelectTpl);
			iTextareaTplFn = doT.template(iTextareaTpl);
			itemListTplFn = doT.template(itemListTpl);
			imageTplFn = doT.template(imageTpl);
			iPictureTplFn = doT.template(iPictureTpl);
			sCheckboxTplFn = doT.template(sCheckboxTpl);
			sLineTplFn = doT.template(sLineTpl);
			sSelectTplFn = doT.template(sSelectTpl);
			sTextTplFn = doT.template(sTextTpl);
			selectItemTplFn = doT.template(selectItemTpl);
			selectDelItemTplFn = doT.template(selectDelItemTpl);
			sImageTplFn = doT.template(sImageTpl);
			sPictureTplFn = doT.template(sPictureTpl);
			sPictureDelItemTplFn = doT.template(sPictureDelItemTpl);
			sPictureItemTplFn = doT.template(sPictureItemTpl);
			sideBarTplFn = doT.template(sideBarTpl);
			formEditTplFn = doT.template(formEditTpl);
			complateTplFn = doT.template(complateTpl);
			formAnswersTplFn = doT.template(formAnswersTpl);
			answerTplFn = doT.template(answerTpl);
			answersTplFn = doT.template(answersTpl);
			tableTplFn = doT.template(tableTpl);
			textAnswerTplFn = doT.template(textAnswerTpl);
			pageNavTplFn = doT.template(pageNavTpl);
			searchPageTplFn = doT.template(searchPageTpl);
			shareTplFn = doT.template(shareTpl);
			showSideBar();
			bindEvent();
			bindOnbeforeunload();
		}

		function showSideBar() {
			$sideBar.empty();
			$.getJSON('/pa/survey/surveys?site_id=' + siteId, {
				page_size: 100,
				page_no: 1
			}, function(ret) {
				var data = ret.data.dataList;
				$sideBar.append(sideBarTplFn(data));
			})
		}

		function showFormEdit(thisdata, callback) {
			$.getJSON('/pageui/ajax-page-publish-list?site_id=' + siteId, function(KZpages) {
				if (KZpages.data.items.length < 1) {
					var pageEmpty = '<div class="f-mold f-header-opt"><div class="f-header red">您还没有发布页面，请先 <a href="/pageui/?site_id=' + siteId + '">发布页面</a></div></div>';
					$formCon.empty();
					$formCon.append(pageEmpty);

				} else {
					$formCon.empty();
					if (thisdata) {
						thisdata.KZpages = KZpages.data.items;
						$formCon.append(formEditTplFn(thisdata));
						callback();
					} else {
						var data = {};
						data.KZpages = KZpages.data.items;
						data.title = '请输入表单标题';
						data.subtitle = '请输入表单副标题';
						$formCon.append(formEditTplFn(data));
					}
					formLayout();
					bindEidtEvent();
				}

			});

		}

		function bindSearchDocEvent() {
			$('#f_select_opt')
				.on('click', '[data-type="select-page"]', function() {
					$('#f_selectpage').show();
					$('#f_selectdoc').hide();
				})
				.on('click', '[data-type="select-doc"]', function() {
					$('#f_selectpage').hide();
					$('#f_selectdoc').show();
				});
			var $searchWrap = $('.f-search-doc');
			$searchWrap
				.on('keyup', '[data-action="show-doc-list"]', function() {
					var key = $('[data-action="show-doc-list"]').val();

					if (key == '') {
						if ($('.f-search-list')[0]) {
							$('.f-search-list').remove();
						}
						$('[data-action="show-doc-list"]').attr('data-docId', "");
						return false
					} else {
						$.getJSON('/post/ajax-search-post?site_id=' + siteId + '&q=' + key, function(ret) {
							$('.f-search-list').remove();
							if (ret.data.count > 0) {
								$searchWrap.append(searchPageTplFn(ret.data))
							}
						});
					}

				})
				.on('click', '[data-action="seach-item"]', function() {
					var docId = $(this).attr('data-docId');
					$('[data-action="show-doc-list"]').val($(this).text());
					$('[data-action="show-doc-list"]').attr('data-docId', docId);
					$('.f-search-list').remove();
				})
		}

		function formLayout() {
			$formBox = $('.f-form-view-bg', $formCon);
			var boxHeight = $(window).height() - $('header').outerHeight() - $('.f-b-nav').outerHeight() - $('.f-btn-wrap').outerHeight() - 100;
			$formBox.height(boxHeight);
			//100 is margin
		}

		function bindEvent() {
			bindAsideEvent();
			bindMaskEvent();
		}

		function bindEidtEvent() {
			drag();
			addItem();
			bindItemEvent();
			settingSelectAction();
			optionSelectAction();
			bindSettingEvent();
			bindEditBtnEvent();
			renderFormTitle();
			bindSearchDocEvent();
			bindCheckTextEvent();
		}

		function bindEditBtnEvent() {
			var $btnWrap = $('.f-form-edit-btn', $formCon);
			var $formClip = $('.f-clip-wrap', $formCon);
			var margin = 0;
			$btnWrap
				.on('click', '[data-action="next"]', function() {
					$formClip.animate({
						'margin-left': margin - 920
					}, 300, function() {
						margin -= 920;
					})
				})
				.on('click', '[data-action="pre"]', function() {
					$formClip.animate({
						'margin-left': margin + 920
					}, 300, function() {
						margin += 920;
					})
				})
				.on('click', '[data-action="creat"]', function() {
					creatSurvey();
				})
				.on('click', '[data-action="publish"]', function() {
					publishSurvey();
				})
		}

		function initItem(item) {
			$setting = $('.f-form-setting');
			$formList = $('.f-form-item-list');
			$setting.empty();
			$formList.find('[current="true"]').removeAttr('current');
		}

		function addItem() {
			$button = $('.f-add-section', $formCon);
			$setting = $('.f-form-setting', $formCon);
			var data = {};
			data.title = '默认标题';
			data.placeholder = '默认文本';
			data.value = [{
				name: '默认选项'
			}];
			data.type = 'text';
			// data.value = [{
			// 	name: '默认选项'
			// }]
			data.default = '默认选项';
			data.required = 1;
			data.description = '';
			data.descriptions = '输入对图片的相关描述';
			data.url = 'http://www.kuaizhan.com/';
			$button
				.on('click', '[data-action="add-input"]', function() {
					initItem();
					$setting.append(sTextTplFn(data));
					$button.before(iInputTplFn(data));
					scrollToView();
					isSave = false;
				})
				.on('click', '[data-action="add-checkbox"]', function() {
					initItem();
					$setting.append(sCheckboxTplFn(data));
					$button.before(iCheckboxTplFn(data));
					scrollToView();
					isSave = false;
				})
				.on('click', '[data-action="add-select"]', function() {
					initItem();
					$setting.append(sSelectTplFn(data));
					$button.before(iSelectTplFn(data));
					scrollToView();
					isSave = false;
				})
				.on('click', '[data-action="add-line"]', function() {
					initItem();
					$setting.append(sLineTplFn(data));
					$button.before(iLineTplFn(data));
					scrollToView();
					isSave = false;
				})
				.on('click', '[data-action="add-image"]', function() {
					initItem();
					$setting.append(sImageTplFn(data));
					$button.before(imageTplFn(data));
					scrollToView();
					isSave = false;
				})
				.on('click', '[data-action="add-picture"]', function() {
					initItem();
					$setting.append(sPictureTplFn(data));
					$button.before(iPictureTplFn(data));
					scrollToView();
					isSave = false;
				});
		}

		function bindCheckTextEvent() {
			$('#form_title_ipt').find('input').on('keyup focusout', function() {
				checkTextNum(this, 15)
			});
			$('#form_subtitle_ipt').find('.f-textarea').on('keyup focusout', function() {
				checkTextNum(this, 100)
			})
		}

		function checkTextNum(ipt, num) {
			var self = $(ipt);
			var text = self.closest('.f-control-group').find('.f-control-tips').find('span');
			text.text(self.val().length);
			if (self.val().length > num) {
				text.addClass('red')
			} else if (text.hasClass('red')) {
				text.removeClass('red');
			}
			self.val(self.val().substring(0, num));
			text.text(self.val().length);
			text.removeClass('red');
			// self.on('focusout keyup', function() {
			// 	self.val(self.val().substring(0, num));
			// 	text.text(self.val().length);
			// 	text.removeClass('red');
			// })
		}

		function scrollToView() {
			$formBox = $('.f-form-view-bg');
			$formList = $('.f-form-item-list');
			var scrollTop = $formList.height() - $formBox.height() + 100;
			$formBox.scrollTop(scrollTop);
			//$('[current="true"]', $formBox)[0].scrollIntoView();
		}

		function settingSelectAction() {
			$setting = $('.f-form-setting');
			select.selectAction($setting, updataItem);
		}

		function optionSelectAction() {
			var $optionWrap = $('.f-option-opt');
			select.selectAction($optionWrap);
		}

		function itemData(mask) {
			var $thisItem = mask.closest('.f-form-item');
			var data = {};
			$setting = $('.f-form-setting');
			$formList = $('.f-form-item-list');
			$('[current="true"]', $formList).removeAttr('current');
			mask.closest('.f-form-item').attr('current', 'true');
			$setting.empty();

			if ($thisItem.attr('data-name') == 'id_checkbox' || $thisItem.attr('data-name') == 'id_radio') {
				data.value = [];
				$.each($('[data-type="list"]', $thisItem).find('label'), function(index, label) {
					data.value.push({
						name: $(label).find('span').text(),
						img: $(label).find('img').attr('src')
					});
				})
			};

			if ($thisItem.attr('data-name') == 'id_dropdown') {
				data.default = $('.f-select', $thisItem).find('em').text();
				data.value = [];
				$.each($('[data-type="select"]', $thisItem).find('li'), function(index, option) {
					data.value.push({
						name: $(option).text()
					})
				})
			}
			if ($thisItem.attr('data-name') == 'id_text') {
				data.type = $thisItem.find('input').attr('type');
			}
			if ($thisItem.attr('data-name') == 'id_picture') {
				data.url = $thisItem.find('a').attr('href');
				data.img = $thisItem.find('img').attr('src');
				data.descriptions = $thisItem.find('span').text();
			}
			if ($thisItem.attr('data-name') == 'id_image') {
				data.img = $thisItem.find('img').attr('src');
				data.description = $thisItem.find('.f-description').find('span').text();
				data.value = $thisItem.find('img').attr('src');
			}

			data.title = $thisItem.find('h2').find('em').text();
			data.required = $thisItem.find('h2').find('span')[0] ? 1 : 0;
			data.name = $thisItem.attr('data-name');
			data.placeholder = '默认文本';


			var thisData = {
				data: data
			};
			return thisData;

		}

		function bindItemEvent() {
			$setting = $('.f-form-setting');
			$formList = $('.f-form-item-list');
			$formList
				.on('click', '[data-name="id_text"]', function() {
					var self = $(this);
					$setting.append(sTextTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_textarea"]', function() {
					var self = $(this);
					$setting.append(sTextTplFn(itemData(self).data));
					$('#f_text_type', $setting).hide();
				})
				.on('click', '[data-name="id_dropdown"]', function() {
					var self = $(this);
					$setting.append(sSelectTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_radio"]', function() {
					var self = $(this);
					$setting.append(sCheckboxTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_checkbox"]', function() {
					var self = $(this);
					$setting.append(sCheckboxTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_section"]', function() {
					var self = $(this);
					$setting.append(sLineTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_image"]', function() {
					var self = $(this);
					$setting.append(sImageTplFn(itemData(self).data));
				})
				.on('click', '[data-name="id_picture"]', function() {
					var self = $(this);
					$setting.append(sPictureTplFn(itemData(self).data));
				})
				.on('click', '.f-item-del', function() {
					var self = $(this);
					$('[data-type="curr"]', $formList).removeAttr('data-type');
					self.closest('.f-form-item').remove();
					$setting.empty();
					return false;
				})
		}

		function settingData() {
			$setting = $('.f-form-setting');
			$formList = $('.f-form-item-list');
			//var $Ele = $('[data-type="curr"]', $formList);
			var data = {};
			var title = $('[data-name="title"]').val();
			var thisName = $('[option-name]', $setting).attr('option-name');
			if (title) {
				data.title = title
			} else {
				data.title = $('[data-name="title"]').attr('placeholder');
			}
			data.required = $('[data-required]', $setting).attr('data-required');
			data.name = thisName;
			data.placeholder = '默认文本';
			if (thisName == 'id_checkbox' || thisName == 'id_radio') {
				data.value = [];
				$.each($('.f-setting-option', $setting).find('[data-name="option"]'), function(index, label) {
					var thisImg = $(label).closest('.f-setting-option').find('img').attr('src');
					if ($(label).val()) {
						data.value.push({
							name: $(label).val(),
							img: thisImg
						});
					} else {
						data.value.push({
							name: $(label).attr('placeholder'),
							img: thisImg
						});
					}

				})
			}
			if (thisName == 'id_dropdown') {
				data.value = [];
				$.each($('.f-setting-option', $setting).find('[data-name="option"]'), function(index, label) {
					if ($(label).val()) {
						data.value.push({
							name: $(label).val()
						});
					} else {
						data.value.push({
							name: $(label).attr('placeholder')
						});
					}
				})
				data.default = data.value[0].name;
			}
			if (thisName == 'id_text') {
				data.type = $('[text-type]', $setting).attr('text-type');
			}
			if (thisName == 'id_picture') {
				data.descriptions = $('.f-textarea', $setting).val();
				data.img = $('img', $setting).attr('src');
				data.url = $('[data-name="url"]', $setting).val();
			}
			if (thisName == 'id_image') {
				data.description = $('[data-name="description"]').val();
				data.img = $('img', $setting).attr('src');
				data.value = $('img', $setting).attr('src');
			}

			var thisData = {
				data: data
			};
			return thisData;

		}

		function updataItem() {
			$setting = $('.f-form-setting');
			$formList = $('.f-form-item-list');
			var thisName = $('[option-name]', $setting).attr('option-name');
			var $thisItem;
			if (thisName == 'id_checkbox') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iCheckboxTplFn(settingData().data));
			}
			if (thisName == 'id_section') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iLineTplFn(settingData().data));
			}
			if (thisName == 'id_text') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iInputTplFn(settingData().data));
			}
			if (thisName == 'id_radio') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iRadioTplFn(settingData().data));
			}
			if (thisName == 'id_dropdown') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iSelectTplFn(settingData().data));
			}
			if (thisName == 'id_textarea') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iTextareaTplFn(settingData().data));
			}
			if (thisName == 'id_image') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(imageTplFn(settingData().data));
			}
			if (thisName == 'id_picture') {
				$thisItem = $('[current="true"]', $formList);
				$thisItem.replaceWith(iPictureTplFn(settingData().data));
			}
		}

		function bindSettingEvent() {
			var mask = '<div class=f-form-view-mask></div>';
			$setting = $('.f-form-setting');
			$formBox = $('.f-form-view-bg');
			$setting
				.on('keyup focusout', '[type="text"]', function() {
					updataItem();
					if ($(this).attr('data-name') == 'title') {
						checkTextNum(this, 25);
					} else if ($(this).attr('data-name') == 'option') {
						checkTextNum(this, 20);
					} else if(($(this).attr('data-name') == 'description')||($(this).attr('data-name') == 'descriptions')){
						checkTextNum(this, 100);
					}
					isSave = false;
				})
				.on('focusin', '[type="text"]', function() {
					$formBox.append(mask)
				})
				.on('focusout', '[type="text"]', function() {
					updataItem();
					if ($('.f-form-view-mask')) {
						$('.f-form-view-mask').remove();
					};
				})

			//var $addBtn = $('[data-action="add-item"]');
			//var $delBtn = $('[data-action="del-item"]');
			$setting.on('click', '[data-action="del-item"]', function() {
					var self = $(this);
					if ($('.f-setting-option', $setting).length > 1) {
						self.closest('.f-setting-option').remove();
						updataItem();
					} else {
						alert('最少需要有一个选项')
					}
					isSave = false;
				})
				.on('click', '[data-action="add-item"]', function() {
					var self = $(this);
					var data = {};
					if ($('.f-setting-option', $setting).length < 10) {
						var $group = self.closest('.f-control-group');
						if (self.closest('.f-control-group').hasClass('f-control-group-select')) {
							data.img = false;
							$group.append(selectItemTplFn(data));
						} else {
							data.img = true;
							$group.append(selectItemTplFn(data));
						}

						updataItem();
					} else {
						alert('最多不可以超过10个选项')
					}
					isSave = false;

				})
				.on('change', '[data-action="add-pic"]', function() {
					uploadPic(this);
					isSave = false;
				})
				.on('click', '[data-action="del-pic"]', function() {
					delPic(this);
					isSave = false;
				})
				.on('change', '[data-action="add-picture"]', function() {
					uploadPicture(this);
					isSave = false;
				})
				.on('click', '[data-action="del-picture"]', function() {
					delPicture(this);
					isSave = false;
				})
				.on('click', '[option-name="id_text"]', function() {
					$('#f_text_type').show();
					isSave = false;
				})
				.on('click', '[option-name="id_textarea"]', function() {
					$('#f_text_type').hide();
					isSave = false;
				})
		}

		function renderFormTitle() {
			var $viewTitle = $('.f-title', $('.f-body-wrap'));
			var $formTitle = $('.f-header', $('.f-header-opt'));
			var $title = $('.f-input', $formTitle);
			var $subTitle = $('.f-textarea', $formTitle);
			var titleData;
			var subTitleData;
			$formTitle
				.on('focusout', '.f-input', function() {
					var tmpTitle = $viewTitle.find('h1').text();
					titleData = $title.val();
					if (titleData == '') {
						$viewTitle.find('h1').text('请输入表单标题');
					} else {
						$viewTitle.find('h1').text(titleData);
						if (tmpTitle != titleData) {
							isSave = false;
						}
					}

				})
				.on('focusout', '.f-textarea', function() {
					var tmpTitle = $viewTitle.find('p').text();
					subTitleData = $subTitle.val();
					if (subTitleData == '') {
						$viewTitle.find('p').text('请输入表单副标题');
					} else {
						$viewTitle.find('p').text(subTitleData);
					}

					if (tmpTitle != subTitleData) {
						isSave = false;
					}
				})
		}

		function creatFormTitle(data, index, item) {
			data.id = index;
			data.title = $(item).find('h2').find('em').text();
			data.instruct = "";
			if ($(item).find('h2').find('span')[0]) {
				data.required = true
			} else {
				data.required = false
			}
			return data;
		}

		function creatFormLabel(item, valueList) {
			var $valueList = $(item).find('label');
			$.each($valueList, function(index, item) {
				var value = {};
				value.lid = index;
				value.name = $(item).find('span').text();
				value.img = $(item).find('em img').attr('src');
				value.selected = false;
				valueList.push(value);
			})
			return valueList;
		}

		function creatFormData() {
			var formData = {};
			var fields = [];
			var valueList = [];
			$formList = $('.f-form-item-list');
			$formBox = $('.f-form-view-bg');
			var $formTitle = $('.f-title', $formBox);
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

					creatFormTitle(data, index, item);
					creatFormLabel(item, valueList);
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
					creatFormTitle(data, index, item);
					creatFormLabel(item, valueList);
					data.value = valueList;
				}
				if (dataName == 'id_section') {
					data = {};
					data.name = 'id_section';
					data.id = index;
					data.title = $(item).find('h2').find('em').text();
				}
				if (dataName == 'id_text') {
					data = {};
					data.name = 'id_text';
					data.type = $(item).find('input').attr('type');
					creatFormTitle(data, index, item);
				}
				if (dataName == 'id_dropdown') {
					valueList = [];
					data = {};
					data.name = 'id_dropdown';
					creatFormTitle(data, index, item);
					var $option = $(item).find('li');
					var optionList = [];
					$.each($option, function(index, item) {
						var option = {};
						option.lid = index;
						option.name = $(item).text();
						if (index == 0) {
							option.selected = false
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
					creatFormTitle(data, index, item);
				}
				if (dataName == 'id_image') {
					data = {};
					data.name = 'id_image';
					creatFormTitle(data, index, item);
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


			formData.title = $formTitle.find('h1').text();
			formData.subtitle = $formTitle.find('p').text();
			if (formData.subtitle == '请输入表单副标题') {
				formData.subtitle = '';
			}
			formData.title_visible = true;
			formData.published = false;
			formData.fields = fields;
			var pageId, postId;
			var searchType = $('#f_select_opt').find('em').attr('data-type');
			if (searchType == ('select-doc')) {
				postId = $('#f_selectdoc').find('.f-input').attr('data-docId');
				if (postId) {
					formData.after_submit = {
						type: 'post_id',
						value: postId
					}
				} else {
					pageId = $('#f_selectpage').find('em').attr('page-id');
					formData.after_submit = {
						type: 'page_id',
						value: pageId
					};
				}
			} else {
				pageId = $('#f_selectpage').find('em').attr('page-id');
				formData.after_submit = {
					type: 'page_id',
					value: pageId
				};
			}

			if ($formCon.attr('surveyId')) {
				formData.id = $formCon.attr('surveyId')
			}

			var body = JSON.stringify(formData);

			// var HTML_CHARS = {
			// 	'<': '&lt;',
			// 	'>': '&gt;',
			// }

			// return (body + '').replace(/[<>]/g, function($0) {
			// 	return HTML_CHARS[$0];
			// });

			return body;
		}

		function creatSurvey() {
			var body = creatFormData();
			ajax.creatSurveys(siteId, body, function(ret) {
				showSideBar();
				$formCon.attr('surveyId', ret.data.id);
				isSave = true;
			}, function() {})
		}

		function publishSurvey() {
			var body = creatFormData();
			var surveyId;
			if ($formCon.attr('surveyId')) {
				surveyId = $formCon.attr('surveyId');
			} else {
				surveyId = 0;
			}
			var data = {};
			if ($('#form_title_ipt').find('input').val() == "") {
				alert('您还没有填写表单的名称')
			} else if ($('.f-form-item-list').find('.f-form-item').length <= 0) {
				alert('您还没有添加任何一项表单内容')
			} else {
				ajax.publish(surveyId, siteId, body, function(ret) {
					data = ret.data;
					data.siteId = siteId;
					showSideBar();
					$formCon.empty();
					$formCon.append(complateTplFn(data));
					bindComplateEvent(ret.data);
					isSave = true;
				}, function() {});
			}

		}

		function bindComplateEvent(data) {
			copyUrl();
			share(data);
		}

		function copyUrl() {
			$('#f-copy-url').zclip({
				path: "/pf/survey/appsrc/js/plugins/ZeroClipboard.swf",
				copy: function() {
					return $(this).prev().val();
				}
			});
		}

		function share(data) {
			var getData = {};
			getData.title = '大家好，这是我的' + data.title + '（调查问卷），欢迎大家填写。';
			getData.pic = $('#f_qrcode').attr('src');
			getData.url = $('.f-complate-input').val();
			$('.f-complete-wrap')
				.on('click', '.f-outside-wb', function() {
					Share.shareTo('weibo', getData);
				})
				.on('click', '.f-outside-rr', function() {
					Share.shareTo('renren', getData);
				})
				.on('click', '.f-outside-qz', function() {
					Share.shareTo('qq', getData);
				})
				.on('click', '.f-outside-db', function() {
					Share.shareTo('douban', getData);
				})
				.on('click', '.f-outside-qwb', function() {
					Share.shareTo('qwb', getData);
				})
		}

		function drag() {
			var $dragWrap = $('.f-form-item-list');
			$dragWrap.sortable({
				revert: true,
				axis: 'y',
				distance: 20,
				opacity: 0.4,
				cursor: 'move',
				scroll: true,
				scrollSensitivity: 40,
				items: ".f-form-item",
				placeholder: 'f-form-placeholder'
			});
		}

		function uploadPic(ipt) {
			var formdata = new FormData();
			var thisText = $(ipt).closest('.f-setting-option').find('[type="text"]').val();
			formdata.append('image', ipt.files[0]);
			ajax.uploadPic(siteId, formdata, function(ret) {
				var thisImg = {};;
				thisImg.img = ret.data;
				thisImg.text = thisText;
				$(ipt).closest('.f-setting-option').replaceWith(selectDelItemTplFn(thisImg));
				updataItem();
			}, function() {
				alert('文件超过5M或者格式有误，请重新选择图片！');
			}, {
				processData: false,
				contentType: false
			})
		}

		function delPic(item) {
			var offset = $(item).offset();
			var thisText = $(item).closest('.f-setting-option').find('[type="text"]').val();
			Alert.show({
				msg: '确定要删除吗?',
				top: offset.top,
				left: offset.left,
				confirmWidth: '200px'
			}, function() {
				var data = {}
				data.img = true;
				data.text = thisText;
				$(item).closest('.f-setting-option').replaceWith(selectItemTplFn(data));
				updataItem();
			});
		}

		function uploadPicture(ipt) {
			var formdata = new FormData();
			var thisText = $(ipt).closest('.f-setting-option').find('[type="text"]').val();
			formdata.append('image', ipt.files[0]);
			ajax.uploadPic(siteId, formdata, function(ret) {
				var thisImg = {};
				thisImg.img = ret.data;
				thisImg.text = thisText;
				$(ipt).closest('.f-setting-option').replaceWith(sPictureDelItemTplFn(thisImg));
				updataItem();
			}, function() {
				alert('文件超过5M或者格式有误，请重新选择图片！');
			}, {
				processData: false,
				contentType: false
			})
		}

		function delPicture(item) {
			var offset = $(item).offset();
			var thisText = $(item).closest('.f-setting-option').find('[type="text"]').val();
			Alert.show({
				msg: '确定要删除吗?',
				top: offset.top,
				left: offset.left,
				confirmWidth: '200px'
			}, function() {
				var data = {}
				data.img = true;
				data.text = thisText;
				$(item).closest('.f-setting-option').replaceWith(sPictureItemTplFn(data));
				updataItem();
			});
		}

		function bindAsideEvent() {
			$sideBar
				.on('click', '[data-action="del-survey"]', function() {
					var thisId = $(this).closest('dd').attr('data-id');
					var data = {};
					var offset = $(this).offset();
					Alert.show({
						msg: '确定要删除吗?',
						top: offset.top,
						left: offset.left,
						confirmWidth: '200px'
					}, function() {
						ajax.delSurvey(thisId, siteId, data, function() {
							showSideBar();
							showFormEdit();
						})
					});
				})
				.on('click', '[data-action="show-survey"]', function() {
					var self = $(this);
					var offset = $(this).offset();
					var thisId = self.closest('dd').attr('data-id');
					if (isSave) {
						$.getJSON('/pa/survey/forms/' + thisId + '?site_id=' + siteId, function(ret) {
							$formCon.attr('surveyId', ret.data.id);
							$formCon.empty();
							showFormEdit(ret.data, function() {
								$button = $('.f-add-section', $formCon);
								$button.before(itemListTplFn(ret.data.fields))
							});
							$sideBar.find('.curr').removeClass('curr');
							self.closest('dd').addClass('curr');
						})
					} else {
						Alert.show({
							msg: '未保存的数据将会丢失，确认要离开吗？',
							top: offset.top,
							left: offset.left + 130,
							InnerYesBtn: '确认离开',
							InnerNoBtn: '留在此页',
							confirmWidth: '270px'
						}, function() {
							$.getJSON('/pa/survey/forms/' + thisId + '?site_id=' + siteId, function(ret) {
								$formCon.attr('surveyId', ret.data.id);
								$formCon.empty();
								showFormEdit(ret.data, function() {
									$button = $('.f-add-section', $formCon);
									$button.before(itemListTplFn(ret.data.fields))
								});

								$sideBar.find('.curr').removeClass('curr');
								self.closest('dd').addClass('curr');
								isSave = true;
							})
						});
					}

				})
				.on('click', '[data-action="creat-form"]', function() {
					var offset = $(this).offset();
					if (isSave) {
						showFormEdit();
						$formCon.removeAttr('surveyid');
					} else {
						Alert.show({
							msg: '未保存的数据将会丢失，要保存草稿吗？',
							top: offset.top,
							left: offset.left + 130,
							InnerYesBtn: '确认离开',
							InnerNoBtn: '留在此页',
							confirmWidth: '270px'
						}, function() {
							showFormEdit();
							$formCon.removeAttr('surveyid');
							isSave = true;
						});
					};
				})
				.on('click', '[data-action="survey-stat"]', function() {
					var self = $(this);
					var offset = $(this).offset();
					var surveyId = self.closest('dd').attr('data-id');
					var date = formatDate(Math.round(self.closest('dd').attr('data-date')), 'yyyy-MM-dd HH:mm');
					var title = self.text();
					if (isSave) {
						getAnswers(surveyId, {
							page_size: 12,
							order: 'desc',
							range: '1',
							date: date,
							title: title
						}, 'all');
						$sideBar.find('.curr').removeClass('curr');
						$(this).closest('dd').addClass('curr');
					} else {
						Alert.show({
							msg: '未保存的数据将会丢失，要保存草稿吗？',
							top: offset.top,
							left: offset.left + 130,
							InnerYesBtn: '确认离开',
							InnerNoBtn: '留在此页',
							confirmWidth: '270px'
						}, function() {
							getAnswers(surveyId, {
								page_size: 12,
								order: 'desc',
								range: '1',
								date: date,
								title: title
							}, 'all');
							$sideBar.find('.curr').removeClass('curr');
							$(this).closest('dd').addClass('curr');
							isSave = true;
						});
					}

				})
		}

		function getAnswers(surveyId, type, other) {
			$.getJSON('/pa/survey/surveys/' + surveyId + '/answers?site_id=' + siteId, type, function(ret) {
				if (type.order == 'desc') {
					ret.data.sort = true;
					ret.data.reverse = false;
				} else {
					ret.data.sort = false;
					ret.data.reverse = true;
				}
				ret.data.other = other;
				ret.data.range = type.range;
				ret.data.date = type.date;
				ret.data.title = type.title;
				$.each(ret.data.dataList, function(index, item) {
					item.date = formatDate(item.created_at, 'yyyy-MM-dd HH:mm');
				});
				updataAnswerShow(ret.data, surveyId, type, other);
			})
		}

		function updataAnswerShow(data, surveyId, pageData, other) {
			$formCon.empty();
			$formCon.append(formAnswersTplFn(data));
			$formCon.attr('surveyId', surveyId);
			var $answersList = $('.f-model-list');
			$answersList.empty();
			$answersList.append(answersTplFn(data));
			updataAnswersPaging(data, pageData, other);
			bindAnswersEvent(surveyId, pageData);

			var $answerWrap = $('.f-model-show');
			$.getJSON('/pa/survey/forms/' + surveyId + '?site_id=' + siteId, function(ret) {
				ret.data.showTitle = true;
				$answerWrap.empty();
				$answerWrap.append(answerTplFn(ret.data));
			})
		}

		function updataAnswersPaging(data, type, other) {
			var surveyId = $formCon.attr('surveyid');
			var $pageWrap = $('.f-model-paging');
			creatPaging($pageWrap, data);
			$pageWrap
				.on('click', '[data-action="gotopage"]', function() {
					var pageNum = $(this).attr('data-page');
					type.page_no = pageNum;
					$.getJSON('/pa/survey/surveys/' + surveyId + '/answers?site_id=' + siteId, type, function(ret) {
						//ret.data.reverse = true;
						if (type.order) {
							ret.data.sort = true;
							ret.data.reverse = false;
						} else {
							ret.data.sort = false;
							ret.data.reverse = true;
						}
						ret.data.date = type.date;
						ret.data.other = other;
						$.each(ret.data.dataList, function(index, item) {
							item.date = formatDate(item.created_at, 'yyyy-MM-dd HH:mm');
						})
						updataAnswerShow(ret.data, surveyId, type)
					})
				})
		}

		function answerSelectEvent(item, type) {
			var orderVal = $(item).closest('.f-model-edit').find('.f-btn').attr('data-action');
			var order;
			if (orderVal == 'reverse') {
				order = 'asc';
			} else {
				order = 'desc';
			}
			var self = $(item);
			var surveyId = $formCon.attr('surveyId');
			var thisType = self.attr('select-type');
			if (thisType == 'all') {
				getAnswers(surveyId, {
					page_size: 12,
					order: order,
					date: type.date
				}, 'all');
			}
			if (thisType == 'star') {
				getAnswers(surveyId, {
					page_size: 12,
					star: true,
					order: order,
					date: type.date
				}, 'star');
			}
			if (thisType == 'done') {
				getAnswers(surveyId, {
					page_size: 12,
					done: false,
					order: order,
					date: type.date
				}, 'done');
			}
			if (thisType == 'unread') {
				getAnswers(surveyId, {
					page_size: 12,
					unread: true,
					order: order,
					date: type.date
				}, 'unread');
			}
		}

		function formatDate(time, format) {
			var t = new Date(time);
			var tf = function(i) {
				return (i < 10 ? '0' : '') + i
			};
			return format.replace(/yyyy|MM|dd|HH|mm|ss/g, function(a) {
				switch (a) {
					case 'yyyy':
						return tf(t.getFullYear());
						break;
					case 'MM':
						return tf(t.getMonth() + 1);
						break;
					case 'mm':
						return tf(t.getMinutes());
						break;
					case 'dd':
						return tf(t.getDate());
						break;
					case 'HH':
						return tf(t.getHours());
						break;
					case 'ss':
						return tf(t.getSeconds());
						break;
				}
			})
		}

		function bindAnswersEvent(surveyId, type) {
			var $answersWrap = $('.f-view-wrap');
			var surveyId = surveyId;
			var $modelTab = $('.f-model-tab');
			$modelTab.
			on('click', '[select-type]', function() {
				answerSelectEvent(this, type);
			});
			//select.selectAction($answersWrap, answerSelectEvent);
			$answersWrap
				.on('click', '[data-action="show-answer"]', function() {
					var $answerWrap = $('.f-model-show');
					var thisId = $(this).attr('data-id');
					var self = $(this);
					$.getJSON('/pa/survey/surveys/' + surveyId + '/answers/' + thisId + '?site_id=' + siteId, function(ret) {
						$answerWrap.empty();
						$answerWrap.append(answerTplFn(ret.data));
						var data = {
							unread: false
						};
						if (self.find('.f-doc').length) {
							ajax.setStatus(surveyId, siteId, thisId, data, 'read', function() {
								self.find('.f-doc').remove();
							});
						};
						$('.f-model-list').find('.curr').removeClass('curr');
						self.addClass('curr');
					})

				})
				.on('click', '[data-action="getstar"]', function() {
					var self = $(this);
					var thisId = $('.f-show-con').attr('data-id');
					var star = $('.f-show-con').attr('data-star');
					var data = {};
					if (star == 'true') {
						data = {
							star: false
						};
						ajax.setStatus(surveyId, siteId, thisId, data, 'star', function() {
							$('.f-model-list').find('.curr').find('.f-star').find('i').remove();
							$('.f-show-con').attr('data-star', 'false');
							self.find('span').text('星标')
						});
					} else {
						data = {
							star: true
						};
						ajax.setStatus(surveyId, siteId, thisId, data, 'star', function() {
							$('.f-model-list').find('.curr').find('.f-star').append('<i class="iconfont">&#xe610;</i>');
							$('.f-show-con').attr('data-star', 'true');
							self.find('span').text('取消星标')

						});

					}
				})
				.on('click', '[data-action="done"]', function() {
					var self = $(this);
					var thisId = $('.f-show-con').attr('data-id');
					var done = $('.f-show-con').attr('data-done');
					var data = {};
					if (done == 'true') {
						data = {
							done: false
						};
						ajax.setStatus(surveyId, siteId, thisId, data, 'done', function() {
							$('.f-model-list').find('.curr').find('.f-selected').find('i').remove();
							$('.f-show-con').attr('data-done', 'false');
							self.find('span').text('处理')
						});
					} else {
						data = {
							done: true
						};
						ajax.setStatus(surveyId, siteId, thisId, data, 'done', function() {
							$('.f-model-list').find('.curr').find('.f-selected').append('<i class="iconfont">&#xe612;</i>');
							$('.f-show-con').attr('data-done', 'true');
							self.find('span').text('取消处理')
						});

					}
				})
				.on('click', '[data-action="delete"]', function() {
					var thisId = $('.f-show-con').attr('data-id');
					var data = {};
					var offset = $(this).offset();
					var $answerWrap = $('.f-model-show');
					Alert.show({
						msg: '数据删除不可恢复，是否确认?',
						top: offset.top,
						left: offset.left,
						confirmWidth: '250px'
					}, function() {
						ajax.deleteAnswer(surveyId, siteId, thisId, data, function() {
							$('.f-model-list').find('.curr').remove();
							$answerWrap.find('.f-show-con').empty();
						});
					});

				})
				.on('click', '[data-action="reverse"]', function() {
					var other = $(this).closest('.f-model-edit').find('.curr').attr('select-type');

					if (other == 'star') {
						getAnswers(surveyId, {
							page_size: 12,
							order: 'desc',
							star: true,
							date: type.date
						}, other);
					} else if (other == 'done') {
						getAnswers(surveyId, {
							page_size: 12,
							order: 'desc',
							done: false,
							date: type.date
						}, other);
					} else if (other == 'unread') {
						getAnswers(surveyId, {
							page_size: 12,
							order: 'desc',
							unread: true,
							date: type.date
						}, other);
					} else {
						getAnswers(surveyId, {
							page_size: 12,
							order: 'desc',
							date: type.date
						});
					}

					// $.getJSON('/pa/survey/surveys/' + surveyId + '/answers?site_id=' + siteId, {
					// 	page_size: 12,
					// 	order: 'desc'
					// },function(ret) {
					// 	ret.data.sort = true;
					// 	ret.data.reverse = false;
					// 	//ret.data.dataList.reverse();
					// 	$.each(ret.data.dataList, function(index, item) {
					// 		item.date = formatDate(item.created_at, 'yyyy-MM-dd');
					// 	})
					// 	updataAnswerShow(ret.data, surveyId, {
					// 		page_size: 12,
					// 		order: 'desc'
					// 	});
					// })
				})
				.on('click', '[data-action="sort"]', function() {
					var other = $(this).closest('.f-model-edit').find('.curr').attr('select-type');
					if (other == 'star') {
						getAnswers(surveyId, {
							page_size: 12,
							star: true,
							date: type.date
						}, other);
					} else if (other == 'done') {
						getAnswers(surveyId, {
							page_size: 12,
							done: false,
							date: type.date
						}, other);
					} else if (other == 'unread') {
						getAnswers(surveyId, {
							page_size: 12,
							unread: true,
							date: type.date
						}, other);
					} else {
						getAnswers(surveyId, {
							page_size: 12,
							date: type.date
						});
					}
				})
				.on('click', '[data-action="export"]', function() {
					window.open('/pa/survey/surveys/' + surveyId + '/answers/0/export?site_id=' + siteId);
					// var empty = {};
					// ajax.exportExcel(surveyId, siteId, empty, function(){
					// 	var data = {};
					// 	data.url ='/pa/survey/surveys/' + surveyId + '/answers/0/export?site_id=' + siteId;
					// 	$('body').append(exportTplFn(data));
					// 	console.log(1);
					// })
				})
				.on('click', '[data-action="open-share"]', function() {
					var data = {};
					data.surveyId = surveyId;
					data.siteId = siteId;
					data.title = $('.f-side').find('.curr').find('span').text();
					$formCon.append(shareTplFn(data));
					bindShareDlgEvent(data);
				})
				// .on('click', '[data-action="del-all"]', function() {
				// 	//var thisId = $('.f-show-con').attr('data-id');
				// 	var ids = [];
				// 	var delId = $('.f-model-list').find('tr');
				// 	$.each(delId, function(index, item){
				// 		var thisId = $(item).attr('data-id');
				// 		ids.push(thisId);
				// 	});
				// 	console.log(delId);
				// 	var data = {};
				// 	var offset = $(this).offset();
				// 	var $answerWrap = $('.f-model-show');
				// 	Alert.show({
				// 		msg: '确定要删除吗?',
				// 		top: offset.top,
				// 		left: offset.left
				// 	}, function() {
				// 		ajax.deleteAnswer(surveyId, siteId, ids, data, function() {
				// 			$('.f-model-list').find('.curr').remove();
				// 			$answerWrap.find('.f-show-con').empty();
				// 		});
				// 	});
				// })

			var $answerTab = $('.f-view-tab');
			var $answerPage = $('[page-id="answer"]');
			var $tablePage = $('[page-id="table"]')
			$answerTab
				.on('click', '[data-action="show-answer-page"]', function() {
					$answerTab.find('.curr').removeClass('curr');
					$(this).closest('li').addClass('curr');
					$tablePage.hide();
					$answerPage.show();
				})
				.on('click', '[data-action="show-table-page"]', function() {
					var thisId = $sideBar.find('.curr').attr('data-id');
					var self = $(this);
					$.getJSON('/pa/survey/surveys/' + thisId + '/stat?site_id=' + siteId, function(ret) {
						$answerTab.find('.curr').removeClass('curr');
						self.closest('li').addClass('curr');
						$answerPage.hide();
						$tablePage.show();
						$tablePage.empty();
						$.each(ret.data.fields, function(index, item) {
							var count = 0;
							if (item.value) {
								$.each(item.value, function(index, item) {
									count += item.selected_count;
								})
							}
							item.valCount = Math.floor(count);
						})
						updataTable(ret.data);
					})


				});
			$tablePage
				.on('click', '[data-action="show-text"]', function() {
					showTextAnswer(this);
				})
		}

		function bindShareDlgEvent(data) {
			var $shareDlg = $('.f-share-dlg');
			var getData = {};
			getData.title = '大家好，这是我的' + data.title + '（调查问卷），欢迎大家填写。';
			getData.pic = 'http://form.kuaizhan.com/forms/' + data.surveyId + '/qrcode';
			getData.url = 'http://form.kuaizhan.com/forms/' + data.surveyId;
			$shareDlg
				.on('click', '.f-outside-wb', function() {
					Share.shareTo('weibo', getData);
				})
				.on('click', '.f-outside-rr', function() {
					Share.shareTo('renren', getData);
				})
				.on('click', '.f-outside-qz', function() {
					Share.shareTo('qq', getData);
				})
				.on('click', '.f-outside-db', function() {
					Share.shareTo('douban', getData);
				})
				.on('click', '.f-outside-qwb', function() {
					Share.shareTo('qwb', getData);
				});


			$('#share_dlg_clip').zclip({
				path: "/pf/survey/appsrc/js/plugins/ZeroClipboard.swf",
				copy: function() {
					return $(this).prev().text();
				}
			});
			$('[data-action="close-modal"]').on('click', function() {
				$('.f-modal', $('.f-p-wrap')).remove();
				$('.f-modal-mask').remove();
				$('html').css({
					'overflow': 'auto'
				});
			})
		}

		function DateAdd(interval, number, date) {
			switch (interval) {
				case "y ":
					{
						date.setFullYear(date.getFullYear() + number);
						return date;
						break;
					}
				case "q ":
					{
						date.setMonth(date.getMonth() + number * 3);
						return date;
						break;
					}
				case "m ":
					{
						date.setMonth(date.getMonth() + number);
						return date;
						break;
					}
				case "w ":
					{
						date.setDate(date.getDate() + number * 7);
						return date;
						break;
					}
				case "d ":
					{
						date.setDate(date.getDate() + number);
						return date;
						break;
					}
				case "h ":
					{
						date.setHours(date.getHours() + number);
						return date;
						break;
					}
				case "m ":
					{
						date.setMinutes(date.getMinutes() + number);
						return date;
						break;
					}
				case "s ":
					{
						date.setSeconds(date.getSeconds() + number);
						return date;
						break;
					}
				default:
					{
						date.setDate(d.getDate() + number);
						return date;
						break;
					}
			}
		}


		function updataTable(ret) {
			var PV = [];
			var answer = [];
			var tmpObj = $.extend({}, ret.visits, ret.answerCount);
			var tmpdate = [];
			$.each(tmpObj, function(key, value) {
				tmpdate.push(key);
			});
			//console.log(1);
			//console.log(tmpObj);
			tmpdate.sort();
			//console.log(2);
			//console.log(tmpdate);

			var lastStr = tmpdate.pop();
			lastStr = lastStr.replace(/-/g, "/");
			var lastDate = new Date(lastStr);
			//console.log(lastStr);
			//console.log(lastDate);



			var dateKey = [];

			var d30 = new Date(lastDate.setDate(lastDate.getDate() - 30));
			for (var i = 0; i < 30; i++) {
				var d = new Date(d30.setDate(d30.getDate() + 1));
				// console.log(d.getFullYear(), d.getMonth() + 1, d.getDate());
				var nd = d.getDate();
				var nm = d.getMonth() + 1;
				var ny = d.getFullYear();
					if (nm < 10) {
					nm = '0' + nm
				}
				if (nd < 10) {
					nd = '0' + nd
				}
				var ndate = ny + '-' + nm + '-' + nd;
				dateKey.push(ndate)
			}
			//console.log(dateKey);
			// console.log('last' + lastDate);
			// for (var i = 0; i < 30; i++) {
			// 	var DL = DateAdd("d ", -1, lastDate);
			// 	var nd = DL.getDate() + 1;
			// 	var nm = DL.getMonth() + 1;
			// 	var ny = DL.getFullYear();
			// 	if (nm < 10) {
			// 		nm = '0' + nm
			// 	}
			// 	if (nd < 10) {
			// 		nd = '0' + nd
			// 	}
			// 	var ndate = ny + '-' + nm + '-' + nd;
			// 	console.log('DL' + DL);
			// 	console.log(ndate);
			// 	dateKey.push(ndate)
			// }


			var keys = {};
			var date = [];
			$.each(dateKey, function(key, value) {
				keys[value] = 0;
				date.push(value);
			});
			// console.log(keys);
			// console.log(date);

			var answerObj = $.extend({},keys, ret.answerCount);

			var visitsObj = $.extend({},keys, ret.visits);
			// console.log(answerObj);
			// console.log(visitsObj);
			$.each(answerObj, function(index, item) {
				answer.push(item)
			});
			$.each(visitsObj, function(index, item) {
				PV.push(item)
			});

			// console.log('11' +answer);
			// console.log('22'+PV);
			var dateList = [];
			$.each(date, function(index, value) {
				var tmpValue = value.substring(5)
				dateList.push(tmpValue)
			})
			PV = PV.slice(0, 30);
			answer = answer.slice(0, 30);
			//dateList.reverse();
			// console.log(dateList);
			// console.log(dateList.length);
			// console.log(answer);
			// console.log(answer.length);
			// console.log(PV);
			// console.log(PV.length);
			var $tablePage = $('[page-id="table"]');

			$tablePage.append(tableTplFn(ret));

			var $canvas = $('#answerCanvas');
			$canvas.highcharts({
				title: {
					text: ret.title
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					categories: dateList
				},
				yAxis: {
					title: {
						text: ''
					},
					plotLines: [{
						value: 0,
						width: 1,
						color: '#808080'
					}]
				},
				tooltip: {
					valueSuffix: ''
				},
				legend: {
					layout: 'horizontal',
					align: 'top',
					x: 560,
					y: -130,
					verticalAlign: 'middle',
					borderWidth: 0
				},
				series: [{
					name: '反馈数量',
					data: answer
				}, {
					name: '浏览数量',
					data: PV
				}]
			});
			var $dateList = $($('.highcharts-axis-labels')[0]);
			var dataItem = $dateList.find('text');
			$.each(dataItem, function(index, item) {
				if ((index + 1) % 5 && index != 0 && index != 30) {
					$(item).remove();
				} else {
					$(item).attr('y', 272);
				}
			})

		}

		function showTextAnswer(item) {
			var surveyId = $formCon.attr('surveyId');
			var thisId = $(item).attr('data-lid');
			$.getJSON('/pa/survey/surveys/' + surveyId + '/fields/' + thisId + '?site_id=' + siteId, {
				page_size: 9,
				page_no: 1
			}, function(ret) {
				$('.f-p-wrap').append(textAnswerTplFn(ret.data));
				updataTextPaging(ret.data, surveyId, thisId);
				$('html').css({
					'overflow': 'hidden'
				});
			});
		}

		function updataTextPaging(data, surveyId, thisId) {
			var $pageWrap = $('.f-answers-paging');
			creatPaging($pageWrap, data);
			$pageWrap
				.on('click', '[data-action="gotopage"]', function() {
					var pageNum = $(this).attr('data-page');
					$.getJSON('/pa/survey/surveys/' + surveyId + '/fields/' + thisId + '?site_id=' + siteId, {
						page_size: 9,
						page_no: pageNum
					}, function(ret) {
						$('.f-modal').remove();
						$('.f-modal-mask').remove();
						$('.f-p-wrap').append(textAnswerTplFn(ret.data));
						updataTextPaging(ret.data, surveyId, thisId)
					});
				})
			$('[data-action="close-modal"]').on('click', function() {
				$('.f-modal', $('.f-p-wrap')).remove();
				$('.f-modal-mask').remove();
				$('html').css({
					'overflow': 'auto'
				});
			})
		}

		function bindMaskEvent() {
			$('.f-p-wrap').on('click', '[data-action="remove-modal"]', function() {
				$('.f-modal', $('.f-p-wrap')).remove();
				$(this).remove();
				$('html').css({
					'overflow': 'auto'
				});
			})
		}

		function creatPaging(wrap, data) {
			var maxPage = data.totalPageCount;
			if (maxPage > 1) {
				var pageIndex = data.currentPageNo;
				var pageSize = data.currentPageSize;
				var pages;
				var pageData = {};
				pageData.noNext = false;
				pageData.noPre = false;
				pageData.more = false;
				pageData.currentPage = false;
				pageData.max = data.totalPageCount;

				var start = 0,
					end = 0;
				if (maxPage <= 5) {
					pages = [];
					start = 1;
					end = maxPage;
				} else {
					pages = []
					if (pageIndex <= 4) {
						start = 1;
						end = 5;
					} else if (pageIndex >= maxPage - 3) {
						start = maxPage - 4;
						end = maxPage;

					} else {
						start = pageIndex - 2;
						end = pageIndex + 2;
					}
				}
				for (var i = start; i <= end; i++) {
					pages.push({
						currentPage: pageIndex == i ? true : false,
						num: i
					});
				}

				pageData.first = 1;
				pageData.last = maxPage;

				pageData.next = pageIndex + 1;
				if (pageData.next > maxPage) {
					pageData.noNext = true;
					pageData.next = 'unclick'
				}
				pageData.pre = pageIndex - 1;
				if (pageData.pre < 1) {
					pageData.pre = 'unclick';
					pageData.noPre = true;
				}
				pageData.pages = pages;
				wrap.empty();
				wrap.append(pageNavTplFn(pageData));
			} else {
				return false
			}

		}

		function bindOnbeforeunload() {
			$(window).bind('beforeunload', function() {
				if (isSave == false) {
					return '确认离开本页？未保存的内容将会丢失!';
				}
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