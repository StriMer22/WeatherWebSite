$(document).ready(function () {
    $("head").append("<link href='https://fonts.googleapis.com/css?family=Fira+Sans:300,400,500,700' rel='stylesheet'>");
    $('.temp-block').on('click', function (e) {
        $this = $(this);
        $this.addClass('active').siblings().removeClass('active');
        $('#main-menu , #nav-toggle, #h-menu').removeClass('active');
        $('#h-share').removeClass('active');
    });
    $('.temp-wrap').on('click', function (e) {
        $(this).toggleClass('open');
    });
    $('body').on('click', function () {
        $('.temp-wrap').removeClass('open');
    });
    $('.temp-wrap').on('click', function (e) {
        e.stopPropagation();
    });
    $('#nav-toggle').on('click', function (e) {
        $(".blur").toggleClass("the-blur");
        e.preventDefault();
        var $this = $(this);
        $this.toggleClass('active');
        $('#h-menu, #main-menu').toggleClass('active');
        $('.temp-wrap').removeClass('open');
    });
    $(document).on('mouseup', function (e) {
        e.preventDefault();
        var div = $("#main-menu");
        if (!div.is(e.target) && div.has(e.target).length === 0 && $('#nav-toggle').has(e.target).length === 0) {
            $('#main-menu , #nav-toggle, #h-menu').removeClass('active');
            $(".blur").removeClass("the-blur");
        }
    })
    var MenuChangeToResolution = function () {
        if ($(window).width() <= 480) {
            $('.pn-top ul li:first').show();
            $('.page-nav li a').each(function (i, elem) {
                if ($(this).hasClass("active")) {
                    $('.pn-top ul li:first a').text(elem.text);
                }
            })
        } else {
            $('.mob_weater').css('display', 'none');
        }
    }
    $(window).resize(function () {
        MenuChangeToResolution();
    })
    var $nav = $('.text-weather-nav nav ul li a');
    var $tab = $('.text-weather-tab');
    $nav.eq(0).addClass('active');
    $tab.not(":first").hide();
    $nav.on('click', function (event) {
        event.preventDefault();
        var $this = $(this);
        var $href = $this.attr('href');
        $($href).fadeIn().siblings('.text-weather-tab').hide();
        $this.addClass('active').parent().siblings().find('a').removeClass('active');
        $(".blur").removeClass("the-blur");
    });
    $('.forecast-row-inner-wrap, .hour-row-inner-wrap').hide();
    $('.forecast-row.active, .hour-row.active').next('.forecast-row-inner-wrap, .hour-row-inner-wrap').show();
    $('.forecast-row, .hour-row').on('click', function (e) {
        var $this = $(this);
        var $innner = $('.forecast-row-inner-wrap, .hour-row-inner-wrap');
        $this.addClass('active').parent().siblings().find('.forecast-row, .hour-row').removeClass('active');
        var is_visible = $('.forecast-row.active, .hour-row.active').next('.forecast-row-inner-wrap, .hour-row-inner-wrap').is(':visible');
        if (is_visible) {
            $('.hour-row').removeClass('active');
        } else {
            $this.addClass('active');
        }
        $this.parent().find($innner).slideToggle();
        $this.parent().siblings().find($innner).slideUp();
    });
    $('.for-arr-top').on('click', function () {
        $(this).parent().slideUp();
    });
    $(".tab_content").hide();
    if ($(window).width() > 700) {
        $(".tab_drawer_heading[rel^='tab1']").addClass("d_active");
        $("#tab1").show();
    }
    $("ul.tabs li").click(function () {
        $(".tab_content").hide();
        var activeTab = $(this).attr("rel");
        $("#" + activeTab).fadeIn();
        $("ul.tabs li").removeClass("active");
        $(this).addClass("active");
        $(".tab_drawer_heading").removeClass("d_active");
        $(".tab_drawer_heading[rel^='" + activeTab + "']").addClass("d_active");
    });
    $(".tab_drawer_heading").click(function () {
        if ($('#' + ($(this).attr("rel"))).css('display') == 'none' && $(window).width() < 400) {
            $(".tab_content").hide();
            var d_activeTab = $(this).attr("rel");
            $("#" + d_activeTab).fadeIn();
            $(".tab_drawer_heading").removeClass("d_active");
            $(this).addClass("d_active");
            $("ul.tabs li").removeClass("active");
            $("ul.tabs li[rel^='" + d_activeTab + "']").addClass("active");
        } else if ($('#' + ($(this).attr("rel"))).css('display') == 'block' && $(window).width() < 400) {
            $('.tab_content').slideUp();
            $(".tab_drawer_heading").removeClass("d_active");
        } else if ($(window).width() > 400) {
            $(".tab_content").hide();
            var d_activeTab = $(this).attr("rel");
            $("#" + d_activeTab).fadeIn();
            $(".tab_drawer_heading").removeClass("d_active");
            $(this).addClass("d_active");
            $("ul.tabs li").removeClass("active");
            $("ul.tabs li[rel^='" + d_activeTab + "']").addClass("active");
        }
    });
    $('.tab-arrow').on('click', function () {
        $('.tab_content').slideUp();
        $(".tab_drawer_heading").removeClass("d_active");
    });
    $('ul.tabs li').last().addClass("tab_last");
    var $width = $(document).width();
    if ($width <= 480) {
        $('.mob_weater a').on('click', function (e) {
            e.preventDefault();
            $('.pn-top ul li:not(:first), .pn-bot ul li').slideToggle("slow");
        });
    }
    $('.climate-dropdown-top').on('click', function () {
        $('.climate-dropdown-bot').slideToggle();
    });
    $(document).on("click", ".transformer-tabs a[href^='#']:not('.active')", function (event) {
        event.preventDefault();
        var $this = $(this);
        var $hash = $(this).attr('href');
        $this.addClass('active').parent().siblings().find('a').removeClass('active');
        $($hash).addClass("active").siblings().removeClass("active");
        $this.closest("ul").toggleClass("open");
    }).on("click", ".transformer-tabs a.active", function (event) {
        event.preventDefault();
        var $this = $(this);
        $this.closest("ul").toggleClass("open");
    });
    $('.a-popup, .s-popup').magnificPopup({
        removalDelay: 500, callbacks: {
            beforeOpen: function () {
                this.st.mainClass = this.st.el.attr('data-effect');
                $('.temp-wrap').removeClass('open');
                $('.main-menu , #nav-toggle, .h-menu').removeClass('active');
                $(".blur").removeClass("the-blur");
            }, afterClose: function () {
            },
        }, midClick: true
    });
    var popupToggleActive = function () {
        $('.settings-popup-md li a').on('click', function (e) {
            e.preventDefault();
            var $this = $(this);
            $this.addClass('active').parent().siblings().find('a').removeClass('active');
        });
        $('.spr-item').on('click', function (e) {
            e.preventDefault();
            var $this = $(this);
            $this.addClass('active').siblings().removeClass('active');
        });
    };
    popupToggleActive();
    $('.spr-item').on('click', function (e) {
        e.preventDefault();
        var $this = $(this);
        $this.addClass('active').siblings().removeClass('active');
    });
    $('.search-dropdown ul li a').on('click', function (e) {
        e.preventDefault();
        var $this = $(this);
        var $text = $this.text();
        var $words = $text.split(",");
        var $word1 = $words[0];
        var $word2 = $words[1];
        $('.search-text1').text($word1);
        $('.search-text2').text($word2);
        $this.addClass('active').parent().siblings().find('a').removeClass('active');
    });
    $('.search-dropdown ul li a span').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        var $this = $(this);
        var $item = $('.weather-block-favorite')[0] ? $('.weather-block-favorite') : $('.weather-block-width');
        $this.parent().slideUp();
        var $menuIndex = $this.parent().parent().index();
        var block_weater = $('.weather-block-favorite')[0] ? $('.weather-block-favorite') : $('.weather-block-width');
        if (!$('.weather-block-favorite')[0]) {
            console.log('true');
        }
        $('.w' + $menuIndex).remove();
        if ($(window).width() > 480) {
            block_weater.css("width", 100 / ($item.length - 1) + '%');
            if (($item.length - 1) == 0) {
                $('#top-page').animate({height: '-=71px'});
            }
        } else {
            $('#top-page').animate({height: '-=70px'});
        }
    });
    var $dropdown = $('.search-dropdown');
    $('.ht-search-input input, .ht-search-input i').on('click', function () {
        $dropdown.slideToggle();
    });
    $('.dropdown-top').on('click', function (e) {
        e.stopPropagation();
        e.preventDefault();
        $dropdown.slideUp();
    });
    $('.search-dropdown ul li a span').on('click', function (e) {
        e.preventDefault();
        e.stopPropagation();
        $(this).parent().slideUp();
    });
    $('body').click(function () {
        $dropdown.slideUp();
    });
    $('.dropdown-top,.ht-search-input i, .ht-search-input input, .search-dropdown, .search-dropdown ul li a span').click(function (event) {
        event.stopPropagation();
    });
    $(window).on('scroll resize', function () {
        if ($(window).width() > 767) {
            var HeaderTop = 111;
            $(window).scroll(function () {
                if ($(window).scrollTop() > HeaderTop) {
                    $('#fix-menu').css({position: 'fixed', top: '0px'});
                } else {
                    $('#fix-menu').css({position: 'absolute', top: '111px'});
                }
            });
        }
        if ($(window).width() < 767) {
            var HeaderTop = 0;
            $(window).scroll(function () {
                if ($(window).scrollTop() > HeaderTop) {
                    $('#fix-menu').css({position: 'fixed', top: '0px'});
                } else {
                    $('#fix-menu').css({position: 'absolute', top: '0px'});
                }
            });
        }
    });
    $('#h-share').on('click', function (e) {
        e.stopPropagation();
        $(this).toggleClass('active');
        $('.temp-wrap').removeClass('open');
        $('#main-menu , #nav-toggle, #h-menu').removeClass('active');
        $(".blur").removeClass("the-blur");
    });
    $('body').on('click', function () {
        $('#h-share').removeClass('active');
    });
});