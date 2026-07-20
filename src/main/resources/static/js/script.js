// main 슬라이드
$(function() {
    /* ============================================================
       메인 슬라이드 전용 로직 (.main_slide)
       ============================================================ */
    const $mainSection = $('.main_slide');
    const $mainSlides = $mainSection.find('.slide_wrap .slide');
    const $progressBar = $mainSection.find('.bar');
    const $currentNum = $mainSection.find('.page .current');
    const $totalNum = $mainSection.find('.page .total');
    const $toggleBtns = $mainSection.find('.toggle'); // 재생, 일시정지 버튼들

    let mainIndex = 0;
    let duration = 5000; // 5초
    let isPaused = false;

    // 1. 초기 설정 (슬라이드 총 개수)
    $totalNum.text($mainSlides.length < 10 ? '0' + $mainSlides.length : $mainSlides.length);

    // 2. 페이지 번호 업데이트 함수
    function updatePage() {
        let num = mainIndex + 1;
        $currentNum.text(num < 10 ? '0' + num : num);
    }

    // 3. 메인 슬라이드 재생바 애니메이션
    function startProgressBar() {
        if (isPaused) return;
        $progressBar.stop().css('width', '0%').animate({
            width: '100%'
        }, duration, 'linear', function() {
            changeSlide(1);
        });
    }

    // 4. 슬라이드 교체 함수
    function changeSlide(direction) {
        $mainSlides.eq(mainIndex).fadeOut(600).removeClass('active');
        mainIndex = (mainIndex + direction + $mainSlides.length) % $mainSlides.length;
        $mainSlides.eq(mainIndex).fadeIn(600).addClass('active');
        
        updatePage();
        startProgressBar();
    }

    // 5. 버튼 이벤트: 이전/다음
    $mainSection.find('.prev').on('click', function() { changeSlide(-1); });
    $mainSection.find('.next').on('click', function() { changeSlide(1); });

    // 6. 버튼 이벤트: 재생/일시정지 토글
    $toggleBtns.on('click', function() {
        // 현재 클릭한 버튼이 무엇이든 상태를 반전
        isPaused = !isPaused;

        if (isPaused) {
            $progressBar.stop();
            // 일시정지 버튼 숨기고 재생 버튼 보이기 (CSS 제어 필요 시 활용)
            $mainSection.find('img[alt="일시정지"]').parent().hide();
            $mainSection.find('img[alt="재생"]').parent().show();
        } else {
            startProgressBar();
            $mainSection.find('img[alt="일시정지"]').parent().show();
            $mainSection.find('img[alt="재생"]').parent().hide();
        }
    });

    // 초기 실행
    // (CSS에서 재생 버튼은 처음에 숨겨져 있어야 함)
    $mainSection.find('img[alt="재생"]').parent().hide(); 
    startProgressBar();
});

// popup 슬라이드 (✅ 무한루프 + 동적 렌더링 대응)
window.PopupSlider = (function () {
  let gap = 30;
  let index = 0;
  let perPage = 2;
  let totalSlides = 0;

  let isDragging = false;
  let startX = 0;
  let currentX = 0;

  const NS = ".popupSlider";

  function isMobile() {
    return $(window).width() < 768;
  }

  function getItemsPerPage() {
    return isMobile() ? 1 : 2;
  }

  function getEventX(e) {
    return e.type.includes("touch") ? e.originalEvent.touches[0].pageX : e.pageX;
  }

  function getWrapWidth($wrap, $popup) {
    let w = $wrap.width() || 0;
    if (w > 0) return w;

    const prevDisplay = $popup.css("display");
    const prevVis = $popup.css("visibility");

    $popup.css({ display: "block", visibility: "hidden" });
    w = $wrap.width() || 0;
    $popup.css({ display: prevDisplay, visibility: prevVis });

    return w;
  }

  function buildTrack() {
    const $popup = $("#popup");
    const $track = $popup.find(".modal_track");
    const $orig = $track.find(".modal_slide").not(".clone");

    if ($orig.length === 0) return { total: 0 };

    perPage = getItemsPerPage();
    totalSlides = $orig.length;

    $track.find(".clone").remove();

    const $headClones = $orig.slice(0, perPage).clone(true).addClass("clone");
    const $tailClones = $orig.slice(-perPage).clone(true).addClass("clone");

    $track.prepend($tailClones);
    $track.append($headClones);

    index = perPage;

    return { total: $orig.length };
  }

  function applyLayout(total) {
    const $popup = $("#popup");
    const $wrap = $popup.find(".modal_wrap");
    const $track = $popup.find(".modal_track");
    const $slides = $popup.find(".modal_slide");

    perPage = getItemsPerPage();

    const containerWidth = getWrapWidth($wrap, $popup);
    if (!containerWidth || $slides.length === 0) return;

    const slideWidth = (containerWidth - gap * (perPage - 1)) / perPage;

    $track.css({
      display: "flex",
      gap: gap + "px",
      willChange: "transform"
    });

    $slides.css({ width: slideWidth + "px" });
    $slides.find("img").css({ width: "100%", height: "auto", display: "block" });

    // ✅ 모바일: 화살표 숨김 / PC: total > perPage이면 표시
    if (isMobile()) {
      $(".modal_nav").hide();
    } else {
      if (total <= perPage) $(".modal_nav").hide();
      else $(".modal_nav").show();
    }

    // ✅ dot 렌더링
    renderDots(total);
  }

  // ✅ dot 인디케이터 생성
  function renderDots(total) {
    const $popup = $("#popup");

    // 기존 dot 제거
    $popup.find(".modal_dots").remove();

    if (!isMobile() || total <= 1) return;

    let dotsHtml = '<div class="modal_dots">';
    for (let i = 0; i < total; i++) {
      dotsHtml += `<span class="modal_dot${i === 0 ? ' active' : ''}" data-idx="${i}"></span>`;
    }
    dotsHtml += '</div>';

    $popup.find(".modal_bottom").before(dotsHtml);

    // dot 클릭 이동
    $popup.find(".modal_dot").on("click", function () {
      const dotIdx = $(this).data("idx");
      index = dotIdx + perPage; // 클론 offset 반영
      move(true);
      updateDots();
    });
  }

  // ✅ 현재 index 기준으로 dot 활성화
  function updateDots() {
    const $popup = $("#popup");
    const realIndex = ((index - perPage) % totalSlides + totalSlides) % totalSlides;
    $popup.find(".modal_dot").removeClass("active").eq(realIndex).addClass("active");
  }

  function move(animate = true) {
    const $popup = $("#popup");
    const $wrap = $popup.find(".modal_wrap");
    const $track = $popup.find(".modal_track");

    perPage = getItemsPerPage();

    const containerWidth = getWrapWidth($wrap, $popup);
    if (!containerWidth) return;

    const slideWidth = (containerWidth - gap * (perPage - 1)) / perPage;
    const offset = -(index * (slideWidth + gap));

    $track.css({
      transition: animate ? "transform 0.4s ease" : "none",
      transform: `translateX(${offset}px)`
    });

    if (isMobile()) updateDots();
  }

  function bindEvents(total) {
    const $popup = $("#popup");
    const $track = $popup.find(".modal_track");

    $track.on("dragstart" + NS, function (e) { e.preventDefault(); });

    $track.on("transitionend" + NS, function () {
      if (index >= perPage + total) {
        index = perPage;
        move(false);
      } else if (index <= 0) {
        index = total;
        move(false);
      }
      if (isMobile()) updateDots();
    });

    $popup.find(".modal_nav.prev").on("click" + NS, function () {
      index--;
      move(true);
    });

    $popup.find(".modal_nav.next").on("click" + NS, function () {
      index++;
      move(true);
    });

    $track.on("mousedown" + NS + " touchstart" + NS, function (e) {
      isDragging = true;
      startX = getEventX(e);
      currentX = startX;
      $track.css("transition", "none");
    });

    $(document).on("mousemove" + NS + " touchmove" + NS, function (e) {
      if (!isDragging) return;

      currentX = getEventX(e);
      const diff = currentX - startX;

      const $wrap = $popup.find(".modal_wrap");
      const containerWidth = getWrapWidth($wrap, $popup);
      if (!containerWidth) return;

      const slideWidth = (containerWidth - gap * (perPage - 1)) / perPage;
      const baseOffset = -(index * (slideWidth + gap));

      $track.css({
        transition: "none",
        transform: `translateX(${baseOffset + diff}px)`
      });
    });

    $(document).on("mouseup" + NS + " touchend" + NS, function () {
      if (!isDragging) return;
      isDragging = false;

      const diff = currentX - startX;
      if (Math.abs(diff) > 50) {
        if (diff > 0) index--;
        else index++;
      }
      move(true);
    });

    $(window).on("resize" + NS, function () {
      const info = buildTrack();
      if (info.total === 0) return;
      applyLayout(info.total);
      move(false);
    });
  }

  function destroy() {
    const $popup = $("#popup");
    const $track = $popup.find(".modal_track");

    $(window).off(NS);
    $(document).off(NS);
    $popup.find(".modal_nav.prev").off(NS);
    $popup.find(".modal_nav.next").off(NS);
    $track.off(NS);
    $popup.find(".modal_dots").remove();

    isDragging = false;
    startX = 0;
    currentX = 0;
  }

  function init(options = {}) {
    destroy();

    gap = typeof options.gap === "number" ? options.gap : 30;

    const info = buildTrack();
    if (info.total === 0) return;

    applyLayout(info.total);
    bindEvents(info.total);
    move(false);

    setTimeout(() => { applyLayout(info.total); move(false); }, 50);
    setTimeout(() => { applyLayout(info.total); move(false); }, 200);
  }

  function refresh() {
    const info = buildTrack();
    if (info.total === 0) return;
    applyLayout(info.total);
    move(false);
  }

  return { init, destroy, refresh };
})();

// 임상 추천 슬라이드
$(document).ready(function() {
    $('.card_slide').each(function() {
        const $container = $(this);
        const $boardCard = $container.find('.board_card');
        const $indexContainer = $container.find('.slide_index');
        let currentIndex = 0;
        let isDown = false, startX, walk = 0, isDragging = false;

        function getItemsToShow() {
            const width = $(window).width();
            return (width >= 1200) ? 3 : (width >= 768 ? 2 : 1);
        }

        function createIndex() {
            $indexContainer.empty();
            const $items = $boardCard.find('.item');
            const itemsToShow = getItemsToShow();
            const maxIndex = Math.max(0, $items.length - itemsToShow + 1);
            if ($items.length > itemsToShow) {
                $boardCard.css('flex-wrap', 'nowrap');
                for (let i = 0; i < maxIndex; i++) $indexContainer.append('<span></span>');
            } else {
                $boardCard.css({'flex-wrap': 'wrap', 'transform': 'translateX(0)'});
            }
            updateIndexStatus();
        }

        function moveToIndex(idx) {
            const $items = $boardCard.find('.item');
            if ($items.length <= 1) return;
            const step = $items[1].getBoundingClientRect().left - $items[0].getBoundingClientRect().left;
            $boardCard.css({'transition': 'transform 0.5s ease-in-out', 'transform': `translateX(${-idx * step}px)`});
            currentIndex = idx;
            updateIndexStatus();
        }

        function updateIndexStatus() {
            $indexContainer.find('span').removeClass('active').eq(currentIndex).addClass('active');
        }

        // 스와이프 시작
        $container.on('mousedown touchstart', function(e) {
            isDown = true;
            isDragging = false; // 시작 시 드래그 상태 초기화
            $boardCard.css('transition', 'none');
            startX = (e.pageX || e.originalEvent.touches[0].pageX);
        });

        // 스와이프 중
        $(window).on('mousemove touchmove', function(e) {
            if (!isDown) return;
            const x = (e.pageX || e.originalEvent.touches[0].pageX);
            walk = startX - x;

            if (Math.abs(walk) > 10) isDragging = true; // 10px 이상 움직이면 드래그로 간주

            const $items = $boardCard.find('.item');
            const step = $items[1] ? $items[1].getBoundingClientRect().left - $items[0].getBoundingClientRect().left : 0;
            $boardCard.css('transform', `translateX(${-currentIndex * step - walk}px)`);
        });

        // 스와이프 종료
        $(window).on('mouseup touchend', function() {
            if (!isDown) return;
            isDown = false;
            if (Math.abs(walk) > 50) {
                const totalItems = $boardCard.find('.item').length;
                const itemsToShow = getItemsToShow();
                if (walk > 0 && currentIndex < totalItems - itemsToShow) currentIndex++;
                else if (walk < 0 && currentIndex > 0) currentIndex--;
            }
            moveToIndex(currentIndex);
            setTimeout(() => { walk = 0; isDragging = false; }, 10);
        });

        // a 태그 및 이미지 끌림 방지 + 클릭 가로채기
        $container.find('a, img').on('dragstart', (e) => e.preventDefault());
        $container.on('click', 'a', function(e) {
            if (isDragging) { // 드래그 중이었다면 링크 이동 방지
                e.preventDefault();
                e.stopPropagation();
            }
        });

        $indexContainer.on('click', 'span', function() { moveToIndex($(this).index()); });
        $(window).on('resize', function() {
            $boardCard.css('transition', 'none');
            createIndex();
            currentIndex = 0;
            $boardCard.css('transform', 'translateX(0px)');
            setTimeout(() => { $boardCard.css('transition', 'transform 0.5s ease-in-out'); }, 50);
        });

        createIndex();
    });
});

// common 슬라이드
$(function() {
    // 0. 대상 컨테이너를 .solution_slide로 한정
    const $container = $('.solution_slide'); 
    const $slideWrap = $container.find('.slide_wrap');
    const $nextBtn = $container.find('.slide_btn .next');
    const $prevBtn = $container.find('.slide_btn .prev');
    
    const gap = 20;
    const itemsPerView = 4;

    // 1. 해당 컨테이너 내부의 슬라이드만 선택하여 복제
    // (이 부분이 전역 $('.slide')를 사용하면 메인 슬라이드까지 복제되는 원인이었습니다.)
    const $originSlides = $slideWrap.find('.slide'); 
    const $firstClones = $originSlides.slice(0, itemsPerView).clone().addClass('clone');
    const $lastClones = $originSlides.slice(-itemsPerView).clone().addClass('clone');
    
    $slideWrap.append($firstClones).prepend($lastClones);

    let currentIndex = itemsPerView;
    let isPressed = false; 
    let startX, scrollLeft, isMoved = false;

    // 2. 이동 함수
    function moveSlide(animated = true) {
        // 복제된 것들을 포함하여 현재 내부에 있는 슬라이드 너비 계산
        const slideWidth = $slideWrap.find('.slide').outerWidth();
        const offset = currentIndex * (slideWidth + gap);
        const duration = animated ? 400 : 0;
        
        $slideWrap.stop().animate({ scrollLeft: offset }, duration, function() {
            // 원본 슬라이드 개수 기준 체크
            const totalOriginCount = $originSlides.length;
            
            if (currentIndex >= totalOriginCount + itemsPerView) {
                currentIndex = itemsPerView;
                moveSlide(false);
            } else if (currentIndex <= 0) {
                currentIndex = totalOriginCount;
                moveSlide(false);
            }
        });
    }

    moveSlide(false);
    $(window).on('resize', function() { moveSlide(false); });

    // 3. 버튼 클릭 이벤트
    $nextBtn.on('click', function() { currentIndex++; moveSlide(); });
    $prevBtn.on('click', function() { currentIndex--; moveSlide(); });

    // 4. 드래그(마우스/터치) 통합 이벤트
    const getX = (e) => e.originalEvent.touches ? e.originalEvent.touches[0].pageX : e.pageX;

    $slideWrap.on('mousedown touchstart', function(e) {
        isPressed = true;
        isMoved = false;
        startX = getX(e);
        scrollLeft = $slideWrap.scrollLeft();
        $slideWrap.stop().css({ cursor: 'grabbing' });
    });

    $(window).on('mousemove touchmove', function(e) {
        if (!isPressed) return;
        
        const x = getX(e);
        const walk = (x - startX);
        
        if (Math.abs(walk) > 5) {
            isMoved = true;
            if (e.type === 'mousemove') e.preventDefault(); 
            $slideWrap.scrollLeft(scrollLeft - walk);
        }
    });

    $(window).on('mouseup touchend', function() {
        if (!isPressed) return;
        isPressed = false;
        $slideWrap.css({ cursor: 'grab' });

        if (!isMoved) return;

        // 드래그 후 가장 가까운 슬라이드로 자석처럼 붙기
        const slideWidth = $slideWrap.find('.slide').outerWidth() + gap;
        currentIndex = Math.round($slideWrap.scrollLeft() / slideWidth);
        moveSlide(true);
    });

    // 이미지 드래그 방지
    $slideWrap.find('img').on('dragstart', function(e) { e.preventDefault(); });
});

// 게시판 카테고리
$(function() {
    $('[class*="board_tab"] .wrap a').on('click', function(e) {
        e.preventDefault();
        const idx = $(this).index();

        $(this).addClass('active').siblings().removeClass('active');

        // 모든 카드를 숨기고, 선택된 카드만 display: flex로 변경
        $('.board_content [class*="board_card"]').hide().eq(idx).css('display', 'flex');
    });
});

// 지도
$(function() {
    $('.map_tab .wrap a').on('click', function(e) {
        e.preventDefault();
        const idx = $(this).index();

        $(this).addClass('active').siblings().removeClass('active');

        $('.map_wrap .map_content').hide().eq(idx).css('display', 'flex');
    });
});

// 유효성 평가
$(function() {
    $('.entry_tab .wrap a').on('click', function(e) {
        e.preventDefault();
        const idx = $(this).index();

        $(this).addClass('active').siblings().removeClass('active');

        $('.entry_wrap .entry_content').hide().eq(idx).css('display', 'block');
    });
});

// 검색
$(function() {
    const $searchInp = $('.search .box input');
    const $suggest = $('.suggest');

    // 1. 추천 검색어 클릭 시 input에 값 입력
    $('.suggest ul li a').on('click', function(e) {
        e.preventDefault();
        const tagText = $(this).text().replace('# ', ''); // '# ' 부분을 제거하고 텍스트만 추출
        $searchInp.val(tagText); // input에 값 세팅
        $suggest.stop().fadeOut(200); // 입력 후 추천창 닫기
    });

    // 2. 검색창 포커스 이벤트 (기존 유지)
    $searchInp.on('focus', function() { $suggest.stop().fadeIn(200); });
    
    // 3. 외부 클릭 시 닫기 (blur 지연)
    $searchInp.on('blur', function() { 
        setTimeout(function() { $suggest.stop().fadeOut(200); }, 200); 
    });
});


// 카운트
$(function() {
    const $section = $(".main2");
    const $counters = $section.find(".count");

    const animateCount = ($el, target) => {
        const isComma = /,/.test(target);
        const isFloat = target.includes(".");
        const cleanTarget = parseFloat(target.replace(/,/g, ""));
        
        // 시작값 계산 함수 (기존 로직 유지)
        const getSmartStartValue = (val) => {
            if (val < 1000) return 0;
            const digits = Math.floor(Math.log10(val));
            return Math.floor((val * 0.99) / Math.pow(10, digits - 1)) * Math.pow(10, digits - 1);
        };

        const startValue = getSmartStartValue(cleanTarget);

        // jQuery animate 활용
        $({ val: startValue }).animate({ val: cleanTarget }, {
            duration: 2000,
            easing: "swing",
            step: function(now) {
                let current = isFloat ? now.toFixed(2) : Math.floor(now);
                $el.text(isComma ? Number(current).toLocaleString() : current);
            },
            complete: function() {
                $el.text(isComma ? cleanTarget.toLocaleString() : cleanTarget);
            }
        });
    };

    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                $counters.each(function() {
                    const $this = $(this);
                    const original = $this.attr("data-value") || $this.text();
                    $this.attr("data-value", original);
                    animateCount($this, original);
                });
                observer.unobserve(entry.target); // 한 번 실행 후 관찰 중단
            }
        });
    }, { threshold: 0.5 });

    if ($section.length) observer.observe($section[0]);
});

// 모션
$(function() {
    const $elements = $('[class*="fade"]');
    let lastScroll = $(window).scrollTop();

    const checkVisibility = () => {
        const sT = $(window).scrollTop();
        const winH = $(window).height();
        const scrollDown = sT > lastScroll;

        const triggerDown = sT + winH * 0.8;
        const triggerUp = sT + winH * 0.2;

        $elements.each(function() {
            const $el = $(this);
            const elTop = $el.offset().top;
            const elBottom = elTop + $el.outerHeight();
            const $parent = $el.closest('section');
            const secH = $parent.outerHeight();

            // 딜레이 설정
            if (secH < 2400) {
                const idx = $parent.find('[class*="fade"]').index($el);
                $el.css('transition-delay', (idx * 150) + 'ms');
            } else {
                $el.css('transition-delay', '0ms');
            }

            // 노출 여부 판단
            if (scrollDown) {
                if (elTop <= triggerDown && elBottom >= sT) {
                    $el.addClass('show');
                } else if (elBottom < sT) {
                    $el.removeClass('show');
                }
            } else {
                if (elBottom >= triggerUp && elTop <= sT + winH) {
                    $el.addClass('show');
                } else if (elTop > sT + winH) {
                    $el.removeClass('show');
                }
            }
        });
        lastScroll = sT;
    };

    $(window).on('scroll resize', checkVisibility);
    checkVisibility(); // 초기 실행
});

// 사이드
$(function() {
    const $w = $(window),
          $sidebtn = $('.side_btn'),
          $footer = $('#common');

    $sidebtn.css('transition', 'bottom 0.3s ease');

    const handleSideBtn = () => {
        if (!$footer.length) return;
        
        const footerHei = $footer.outerHeight(),
              docH = $(document).height(),
              winH = $w.height(),
              sT = $w.scrollTop();
        
        const limit = docH - winH - footerHei;

        if (sT >= limit) {
            $sidebtn.addClass('on').css('bottom', (footerHei + 10) + 'px');
        } else {
            $sidebtn.removeClass('on').css('bottom', '10px');
        }
    };

    $w.on('scroll resize', handleSideBtn);
    handleSideBtn();

    $('a[href^="#"]').on('click', function() {
        setTimeout(() => $w.trigger('scroll'), 50);
    });
});

// 체크리스트
$(function() {
    const $selectAll = $('#select_all');
    const $itemChecks = $('.custom_check input[type="checkbox"]:not(#select_all)');

    $selectAll.on('change', function() {
        $itemChecks.prop('checked', $(this).prop('checked'));
    });

    $itemChecks.on('change', function() {
        const isAllChecked = $itemChecks.length === $itemChecks.filter(':checked').length;
        $selectAll.prop('checked', isAllChecked);
    });
});

$(function() {
    $('[id*="all_yes"]').on('change', function() {
        const $table = $(this).closest('table');
        if ($(this).is(':checked')) {
            $table.find('.chk_yes').prop('checked', true);
        }
    });

    $('[id*="all_no"]').on('change', function() {
        const $table = $(this).closest('table');
        if ($(this).is(':checked')) {
            $table.find('.chk_no').prop('checked', true);
        }
    });

    $('.chk_yes, .chk_no').on('change', function() {
        const $table = $(this).closest('table');
        $table.find('[id*="all_yes"], [id*="all_no"]').prop('checked', false);
    });
});

// 메뉴
$(function () {
    let isMouseOver = false; // 마우스 오버 상태를 저장하는 변수

    function updateHeader() {
        // 마우스가 올려져 있는 상태라면 스크롤 체크를 무시하고 active 유지
        if (isMouseOver) return; 

        if ($(window).scrollTop() > 10) {
            $('header.pc_menu').addClass('active');
        } else {
            $('header.pc_menu').removeClass('active');
        }
    }

    // 초기 체크
    updateHeader();

    // 스크롤 시 상태 변경
    $(window).on('scroll', function () {
        updateHeader();
    });

    // 마우스 이벤트: 위임 방식을 사용하면 동적 로드된 헤더에도 대응 가능합니다.
    $(document).on('mouseenter', 'header.pc_menu', function () {
        isMouseOver = true; // 마우스 들어옴
        $(this).addClass('active');
    }).on('mouseleave', 'header.pc_menu', function () {
        isMouseOver = false; // 마우스 나감
        updateHeader(); // 나가는 순간의 스크롤 위치에 따라 상태 업데이트
    });
});

$(function () {
    const $header = $('header.m_menu');
    const $menuPanel = $('.m_menu .menu'); // 메뉴판 요소
    const $depth1Item = $('.m_menu .depth1 > a');
    const $body = $('body');

    // 1. 스크롤 이벤트: 헤더 배경색 제어 (.active)
    $(window).on('scroll', function() {
        if ($(window).scrollTop() > 50) {
            $header.addClass('active');
        } else {
            // 메뉴가 열려있는 상태라면 active를 제거하지 않음 (시각적 일관성)
            if (!$menuPanel.hasClass('open')) {
                $header.removeClass('active');
            }
        }
    });

    // 2. 모바일 메뉴 열기 (.open 추가)
    $('.m_open').on('click', function () {
        $header.addClass('active'); // 메뉴 열릴 때 헤더 배경도 흰색으로
        $menuPanel.addClass('open'); // 메뉴판 등장
        $body.css('overflow', 'hidden'); 
        $('.m_open').hide();
        $('.m_close').show();
        openFirstMenu();
    });

    // 3. 모바일 메뉴 닫기 (.open 제거)
    $('.m_close').on('click', function () {
        $menuPanel.removeClass('open'); // 메뉴판 숨김
        $body.css('overflow', '');
        $('.m_close').hide();
        $('.m_open').show();

        // 만약 스크롤이 최상단이라면 헤더의 active도 제거
        if ($(window).scrollTop() <= 50) {
            $header.removeClass('active');
        }
    });

    // 4. 왼쪽 메뉴(Depth1) 클릭 로직 (기존과 동일)
    $depth1Item.on('click', function (e) {
        if ($(this).next('.depth2').length > 0) {
            e.preventDefault();
            $('.depth1 > a').removeClass('active');
            $('.depth2').hide();
            $(this).addClass('active');
            $(this).next('.depth2').show();
        }
    });

    function openFirstMenu() {
        $('.depth1 > a').removeClass('active');
        $('.depth2').hide();
        $('.depth1').eq(0).find('> a').addClass('active');
        $('.depth1').eq(0).find('.depth2').show();
    }
});
// 언어 선택 (구글 번역)
$(function() {
    /**
     * PC 언어 선택 (호버 제거, 클릭만)
     */
    const $pcLangToggle = $('#lang_btn_pc');
    const $pcLangList = $('#lang_list');
    const $pcLangWrapper = $('.lang');
    
    let isPcManuallyToggled = false;
    
    // 클릭으로만 열기/닫기
    if ($pcLangToggle.length) {
        $pcLangToggle.on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            isPcManuallyToggled = !isPcManuallyToggled;
            
            if (isPcManuallyToggled) {
                $pcLangList.addClass('show');
            } else {
                $pcLangList.removeClass('show');
            }
        });
    }
    
    // PC 언어 선택 클릭
    $('#lang_list li').on('click', function(e) {
        e.preventDefault();
        const lang = $(this).data('lang');
        changeLanguage(lang);
    });
    
    // 문서 클릭 시 닫기
    $(document).on('click', function(e) {
        if ($pcLangWrapper.length && !$pcLangWrapper[0].contains(e.target)) {
            $pcLangList.removeClass('show');
            isPcManuallyToggled = false;
        }
    });
    
    /**
     * 모바일 언어 선택
     */
    const $mLangBtn = $('#lang_btn_mobile');
    const $mLangList = $('#m_lang_list');
    const $langOverlay = $('#lang_overlay');
    
    // 모바일 언어 버튼 클릭
    if ($mLangBtn.length) {
        $mLangBtn.on('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            $mLangList.addClass('show');
            $langOverlay.addClass('show');
        });
    }
    
    // 모바일 언어 선택 클릭
    $('#m_lang_list li').on('click', function(e) {
        e.preventDefault();
        const lang = $(this).data('lang');
        changeLanguage(lang);
    });
    
    // 오버레이 클릭 시 닫기
    if ($langOverlay.length) {
        $langOverlay.on('click', function() {
            $mLangList.removeClass('show');
            $langOverlay.removeClass('show');
        });
    }
    
    /**
     * 언어 변경 함수
     */
    function changeLanguage(lang) {
        const targetLang = lang === 'ko' ? 'ko' : lang;
        
        // 쿠키 설정
        document.cookie = `googtrans=/ko/${targetLang}; path=/`;
        
        // 페이지 새로고침
        setTimeout(function() {
            location.reload();
        }, 100);
    }
    
    /**
     * 현재 언어에 따른 body 클래스 추가
     */
    const match = document.cookie.match(/googtrans=\/ko\/([a-z\-A-Z]+)/);
    const currentLang = match ? match[1] : 'ko';
    
    if (currentLang === 'zh-CN') {
        $('body').addClass('lang-zh');
    } else if (currentLang === 'ja') {
        $('body').addClass('lang-ja');
    }
});