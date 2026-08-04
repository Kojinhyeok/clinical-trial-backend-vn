/**
 * 클라이언트 사이드 검색 필터 컴포넌트
 *
 * @param {Object} options
 *   containerId  - 검색 UI를 렌더링할 컨테이너 요소 ID
 *   tableId      - 필터 대상 테이블 ID
 *   pagination   - { perPage, infoId, navId } 페이지네이션 옵션
 *   fields       - 필터 필드 배열
 *   showReset    - 초기화 버튼 표시 여부 (기본 false)
 *
 * fields 항목 구조:
 *   type       - "text" | "select"
 *   id         - 필드 요소 ID (자동생성 가능)
 *   label      - 라벨 텍스트
 *   placeholder - (text) placeholder 텍스트
 *   colClass   - Bootstrap 컬럼 클래스 (기본 "col-md-3")
 *   options    - (select) [{ value, text }] 옵션 배열
 *   targets    - (text) 검색 대상 컬럼 인덱스 배열 (1-based), OR 매칭
 *   dataKey    - data-* 속성 키 (select: exact match, text: substring match)
 *   match      - (select) "contains" 지정 시 부분 일치 (복수 값 지원, 기본: exact)
 *
 * @returns {Object} { applyFilter(), resetFilter(), setFieldValue(id, value) }
 *
 * Usage:
 *   var sf = initSearchFilter({
 *       containerId: "searchFilterContainer",
 *       tableId: "storyTable",
 *       pagination: { perPage: 10, infoId: "pageInfo", navId: "pageNav" },
 *       fields: [
 *           { type: "text", label: "제목", placeholder: "제목 검색", colClass: "col-md-3", targets: [3] },
 *           { type: "select", label: "상태", colClass: "col-md-2", dataKey: "status",
 *             options: [{ value: "published", text: "게시" }, { value: "temp", text: "임시저장" }] }
 *       ]
 *   });
 */
function initSearchFilter(options) {
	var container = document.getElementById(options.containerId);
	if (!container) return null;

	var tableId = options.tableId;
	var pagOpts = options.pagination || {};
	var fields = options.fields || [];
	var showReset = options.showReset || false;
	var pager = null;
	var fieldIdCounter = 0;

	// 필드 ID 자동 생성
	fields.forEach(function (f) {
		if (!f.id) {
			f.id = "sf_field_" + (fieldIdCounter++);
		}
		if (!f.colClass) {
			f.colClass = "col-md-3";
		}
	});

	// UI 렌더링
	renderUI();

	// 페이지네이션 초기화
	if (typeof initPagination === "function") {
		pager = initPagination({
			tableId: tableId,
			perPage: pagOpts.perPage || 10,
			infoId: pagOpts.infoId || "pageInfo",
			navId: pagOpts.navId || "pageNav"
		});
	}

	// 이벤트 바인딩
	bindEvents();

	// feather icons 갱신
	if (window.feather) feather.replace();

	function renderUI() {
		var html = '<div class="card mb-3"><div class="card-body"><div class="row g-2 align-items-end">';

		fields.forEach(function (f) {
			html += '<div class="' + f.colClass + '">';
			html += '<label class="form-label" for="' + f.id + '">' + f.label + '</label>';

			if (f.type === "text") {
				html += '<input type="text" class="form-control" id="' + f.id + '"';
				if (f.placeholder) html += ' placeholder="' + f.placeholder + '"';
				html += '>';
			} else if (f.type === "select") {
				html += '<select class="form-select" id="' + f.id + '">';
				html += '<option value="">전체</option>';
				if (f.options) {
					f.options.forEach(function (opt) {
						html += '<option value="' + opt.value + '">' + opt.text + '</option>';
					});
				}
				html += '</select>';
			}

			html += '</div>';
		});

		// 검색 버튼
		html += '<div class="col-md-2">';
		html += '<button class="btn btn-outline-primary w-100" id="sf_btnSearch">';
		html += '<i class="align-middle" data-feather="search" style="width:14px;height:14px;"></i>';
		html += '<span class="align-middle ms-1">검색</span>';
		html += '</button></div>';

		// 초기화 버튼
		if (showReset) {
			html += '<div class="col-md-2">';
			html += '<button class="btn btn-outline-secondary w-100" id="sf_btnReset">';
			html += '<span class="align-middle">초기화</span>';
			html += '</button></div>';
		}

		html += '</div></div></div>';
		container.innerHTML = html;
	}

	function bindEvents() {
		// 검색 버튼 클릭
		var btnSearch = document.getElementById("sf_btnSearch");
		if (btnSearch) {
			btnSearch.addEventListener("click", applyFilter);
		}

		// text 필드 Enter키
		fields.forEach(function (f) {
			if (f.type === "text") {
				var el = document.getElementById(f.id);
				if (el) {
					el.addEventListener("keypress", function (e) {
						if (e.key === "Enter") applyFilter();
					});
				}
			}
		});

		// 초기화 버튼
		if (showReset) {
			var btnReset = document.getElementById("sf_btnReset");
			if (btnReset) {
				btnReset.addEventListener("click", resetFilter);
			}
		}
	}

	function applyFilter() {
		var rows = document.querySelectorAll("#" + tableId + " tbody tr");

		rows.forEach(function (row) {
			var show = true;

			fields.forEach(function (f) {
				var el = document.getElementById(f.id);
				if (!el) return;
				var val = el.value.trim();
				if (!val) return;

				if (f.type === "text") {
					var keyword = val.toLowerCase();
					var matched = false;

					if (f.dataKey) {
						// data-* 속성 기반 부분 일치
						var dataVal = row.dataset[f.dataKey] || "";
						if (dataVal.toLowerCase().indexOf(keyword) !== -1) matched = true;
					}

					if (f.targets) {
						// 컬럼 내용 기반 부분 일치 (OR)
						f.targets.forEach(function (colIdx) {
							var td = row.querySelector("td:nth-child(" + colIdx + ")");
							if (td && td.textContent.toLowerCase().indexOf(keyword) !== -1) {
								matched = true;
							}
						});
					}

					if (!matched) show = false;

				} else if (f.type === "select") {
					if (f.dataKey) {
						var dataVal = row.dataset[f.dataKey] || "";
						if (f.match === "contains") {
							// data-* 속성 기반 부분 일치 (복수 값 지원)
							if (dataVal.indexOf(val) === -1) show = false;
						} else {
							// data-* 속성 기반 정확 일치 (기본)
							if (dataVal !== val) show = false;
						}
					}
				}
			});

			if (show) {
				row.classList.remove("search-hidden");
			} else {
				row.classList.add("search-hidden");
			}
		});

		if (pager) pager.refresh();
	}

	function resetFilter() {
		fields.forEach(function (f) {
			var el = document.getElementById(f.id);
			if (el) el.value = "";
		});
		applyFilter();
	}

	function setFieldValue(id, value) {
		var el = document.getElementById(id);
		if (el) el.value = value;
	}

	return {
		applyFilter: applyFilter,
		resetFilter: resetFilter,
		setFieldValue: setFieldValue
	};
}
