/*
 Highcharts Gantt JS v8.0.4 (2020-03-10)

 (c) 2017-2018 Lars Cabrera, Torstein Honsi, Jon Arild Nygard & Oystein Moseng

 License: www.highcharts.com/license
*/
(function (Y, P) {
    "object" === typeof module && module.exports ? (P["default"] = P, module.exports = Y.document ? P(Y) : P) : "function" === typeof define && define.amd ? define("highcharts/highcharts-gantt", function () {
        return P(Y)
    }) : (Y.Highcharts && Y.Highcharts.error(16, !0), Y.Highcharts = P(Y))
})("undefined" !== typeof window ? window : this, function (Y) {
    function P(f, k, H, q) {
        f.hasOwnProperty(k) || (f[k] = q.apply(null, H))
    }

    var u = {};
    P(u, "parts/Globals.js", [], function () {
        var f = "undefined" !== typeof Y ? Y : "undefined" !== typeof window ? window : {}, k =
                f.document, H = f.navigator && f.navigator.userAgent || "",
            q = k && k.createElementNS && !!k.createElementNS("http://www.w3.org/2000/svg", "svg").createSVGRect,
            G = /(edge|msie|trident)/i.test(H) && !f.opera, M = -1 !== H.indexOf("Firefox"),
            L = -1 !== H.indexOf("Chrome"), C = M && 4 > parseInt(H.split("Firefox/")[1], 10);
        return {
            product: "Highcharts",
            version: "8.0.4",
            deg2rad: 2 * Math.PI / 360,
            doc: k,
            hasBidiBug: C,
            hasTouch: !!f.TouchEvent,
            isMS: G,
            isWebKit: -1 !== H.indexOf("AppleWebKit"),
            isFirefox: M,
            isChrome: L,
            isSafari: !L && -1 !== H.indexOf("Safari"),
            isTouchDevice: /(Mobile|Android|Windows Phone)/.test(H),
            SVG_NS: "http://www.w3.org/2000/svg",
            chartCount: 0,
            seriesTypes: {},
            symbolSizes: {},
            svg: q,
            win: f,
            marginNames: ["plotTop", "marginRight", "marginBottom", "plotLeft"],
            noop: function () {
            },
            charts: [],
            dateFormats: {}
        }
    });
    P(u, "parts/Utilities.js", [u["parts/Globals.js"]], function (f) {
        function k() {
            var b, a = arguments, m = {}, c = function (a, b) {
                "object" !== typeof a && (a = {});
                W(b, function (m, h) {
                    !y(m, !0) || l(m) || p(m) ? a[h] = b[h] : a[h] = c(a[h] || {}, m)
                });
                return a
            };
            !0 === a[0] && (m = a[1], a = Array.prototype.slice.call(a,
                2));
            var h = a.length;
            for (b = 0; b < h; b++) m = c(m, a[b]);
            return m
        }

        function H(a, b, m) {
            var c;
            v(b) ? d(m) ? a.setAttribute(b, m) : a && a.getAttribute && ((c = a.getAttribute(b)) || "class" !== b || (c = a.getAttribute(b + "Name"))) : W(b, function (b, m) {
                a.setAttribute(m, b)
            });
            return c
        }

        function q() {
            for (var a = arguments, b = a.length, m = 0; m < b; m++) {
                var c = a[m];
                if ("undefined" !== typeof c && null !== c) return c
            }
        }

        function G(a, b) {
            if (!a) return b;
            var m = a.split(".").reverse();
            if (1 === m.length) return b[a];
            for (a = m.pop(); "undefined" !== typeof a && "undefined" !== typeof b &&
            null !== b;) b = b[a], a = m.pop();
            return b
        }

        f.timers = [];
        var M = f.charts, L = f.doc, C = f.win, F = f.error = function (a, b, m, c) {
            var h = n(a), d = h ? "Highcharts error #" + a + ": www.highcharts.com/errors/" + a + "/" : a.toString(),
                x = function () {
                    if (b) throw Error(d);
                    C.console && console.log(d)
                };
            if ("undefined" !== typeof c) {
                var e = "";
                h && (d += "?");
                f.objectEach(c, function (a, b) {
                    e += "\n" + b + ": " + a;
                    h && (d += encodeURI(b) + "=" + encodeURI(a))
                });
                d += e
            }
            m ? f.fireEvent(m, "displayError", {code: a, message: d, params: c}, x) : x()
        }, E = function () {
            function a(a, b, m) {
                this.options =
                    b;
                this.elem = a;
                this.prop = m
            }

            a.prototype.dSetter = function () {
                var a = this.paths[0], b = this.paths[1], m = [], c = this.now, h = a.length;
                if (1 === c) m = this.toD; else if (h === b.length && 1 > c) for (; h--;) {
                    var d = parseFloat(a[h]);
                    m[h] = isNaN(d) || "A" === b[h - 4] || "A" === b[h - 5] ? b[h] : c * parseFloat("" + (b[h] - d)) + d
                } else m = b;
                this.elem.attr("d", m, null, !0)
            };
            a.prototype.update = function () {
                var a = this.elem, b = this.prop, m = this.now, c = this.options.step;
                if (this[b + "Setter"]) this[b + "Setter"](); else a.attr ? a.element && a.attr(b, m, null, !0) : a.style[b] = m + this.unit;
                c && c.call(a, m, this)
            };
            a.prototype.run = function (a, b, m) {
                var c = this, h = c.options, d = function (a) {
                    return d.stopped ? !1 : c.step(a)
                }, x = C.requestAnimationFrame || function (a) {
                    setTimeout(a, 13)
                }, e = function () {
                    for (var a = 0; a < f.timers.length; a++) f.timers[a]() || f.timers.splice(a--, 1);
                    f.timers.length && x(e)
                };
                a !== b || this.elem["forceAnimate:" + this.prop] ? (this.startTime = +new Date, this.start = a, this.end = b, this.unit = m, this.now = this.start, this.pos = 0, d.elem = this.elem, d.prop = this.prop, d() && 1 === f.timers.push(d) && x(e)) : (delete h.curAnim[this.prop],
                h.complete && 0 === Object.keys(h.curAnim).length && h.complete.call(this.elem))
            };
            a.prototype.step = function (a) {
                var b = +new Date, m = this.options, c = this.elem, h = m.complete, d = m.duration, x = m.curAnim;
                if (c.attr && !c.element) a = !1; else if (a || b >= d + this.startTime) {
                    this.now = this.end;
                    this.pos = 1;
                    this.update();
                    var e = x[this.prop] = !0;
                    W(x, function (a) {
                        !0 !== a && (e = !1)
                    });
                    e && h && h.call(c);
                    a = !1
                } else this.pos = m.easing((b - this.startTime) / d), this.now = this.start + (this.end - this.start) * this.pos, this.update(), a = !0;
                return a
            };
            a.prototype.initPath =
                function (a, b, m) {
                    function c(a) {
                        for (r = a.length; r--;) {
                            var b = "M" === a[r] || "L" === a[r];
                            var m = /[a-zA-Z]/.test(a[r + 3]);
                            b && m && a.splice(r + 1, 0, a[r + 1], a[r + 2], a[r + 1], a[r + 2])
                        }
                    }

                    function h(a, b) {
                        for (; a.length < T;) {
                            a[0] = b[T - a.length];
                            var m = a.slice(0, g);
                            [].splice.apply(a, [0, 0].concat(m));
                            O && (m = a.slice(a.length - g), [].splice.apply(a, [a.length, 0].concat(m)), r--)
                        }
                        a[0] = "M"
                    }

                    function d(a, b) {
                        for (var m = (T - a.length) / g; 0 < m && m--;) w = a.slice().splice(a.length / K - g, g * K), w[0] = b[T - g - m * g], R && (w[g - 6] = w[g - 2], w[g - 5] = w[g - 1]), [].splice.apply(a,
                            [a.length / K, 0].concat(w)), O && m--
                    }

                    b = b || "";
                    var x = a.startX, e = a.endX, R = -1 < b.indexOf("C"), g = R ? 7 : 3, w, r;
                    b = b.split(" ");
                    m = m.slice();
                    var O = a.isArea, K = O ? 2 : 1;
                    R && (c(b), c(m));
                    if (x && e) {
                        for (r = 0; r < x.length; r++) if (x[r] === e[0]) {
                            var t = r;
                            break
                        } else if (x[0] === e[e.length - x.length + r]) {
                            t = r;
                            var z = !0;
                            break
                        } else if (x[x.length - 1] === e[e.length - x.length + r]) {
                            t = x.length - r;
                            break
                        }
                        "undefined" === typeof t && (b = [])
                    }
                    if (b.length && n(t)) {
                        var T = m.length + t * K * g;
                        z ? (h(b, m), d(m, b)) : (h(m, b), d(b, m))
                    }
                    return [b, m]
                };
            a.prototype.fillSetter = function () {
                f.Fx.prototype.strokeSetter.apply(this,
                    arguments)
            };
            a.prototype.strokeSetter = function () {
                this.elem.attr(this.prop, f.color(this.start).tweenTo(f.color(this.end), this.pos), null, !0)
            };
            return a
        }();
        f.Fx = E;
        f.merge = k;
        var A = f.pInt = function (a, b) {
            return parseInt(a, b || 10)
        }, v = f.isString = function (a) {
            return "string" === typeof a
        }, B = f.isArray = function (a) {
            a = Object.prototype.toString.call(a);
            return "[object Array]" === a || "[object Array Iterator]" === a
        }, y = f.isObject = function (a, b) {
            return !!a && "object" === typeof a && (!b || !B(a))
        }, p = f.isDOMElement = function (a) {
            return y(a) &&
                "number" === typeof a.nodeType
        }, l = f.isClass = function (a) {
            var b = a && a.constructor;
            return !(!y(a, !0) || p(a) || !b || !b.name || "Object" === b.name)
        }, n = f.isNumber = function (a) {
            return "number" === typeof a && !isNaN(a) && Infinity > a && -Infinity < a
        }, e = f.erase = function (a, b) {
            for (var m = a.length; m--;) if (a[m] === b) {
                a.splice(m, 1);
                break
            }
        }, d = f.defined = function (a) {
            return "undefined" !== typeof a && null !== a
        };
        f.attr = H;
        var g = f.splat = function (a) {
                return B(a) ? a : [a]
            }, c = f.syncTimeout = function (a, b, m) {
                if (0 < b) return setTimeout(a, b, m);
                a.call(0, m);
                return -1
            },
            b = f.clearTimeout = function (a) {
                d(a) && clearTimeout(a)
            }, a = f.extend = function (a, b) {
                var m;
                a || (a = {});
                for (m in b) a[m] = b[m];
                return a
            };
        f.pick = q;
        var w = f.css = function (b, m) {
            f.isMS && !f.svg && m && "undefined" !== typeof m.opacity && (m.filter = "alpha(opacity=" + 100 * m.opacity + ")");
            a(b.style, m)
        }, z = f.createElement = function (b, m, c, h, d) {
            b = L.createElement(b);
            m && a(b, m);
            d && w(b, {padding: "0", border: "none", margin: "0"});
            c && w(b, c);
            h && h.appendChild(b);
            return b
        }, D = f.extendClass = function (b, m) {
            var c = function () {
            };
            c.prototype = new b;
            a(c.prototype,
                m);
            return c
        }, r = f.pad = function (a, b, m) {
            return Array((b || 2) + 1 - String(a).replace("-", "").length).join(m || "0") + a
        }, h = f.relativeLength = function (a, b, m) {
            return /%$/.test(a) ? b * parseFloat(a) / 100 + (m || 0) : parseFloat(a)
        }, t = f.wrap = function (a, b, m) {
            var c = a[b];
            a[b] = function () {
                var a = Array.prototype.slice.call(arguments), b = arguments, h = this;
                h.proceed = function () {
                    c.apply(h, arguments.length ? arguments : b)
                };
                a.unshift(c);
                a = m.apply(this, a);
                h.proceed = null;
                return a
            }
        }, I = f.format = function (a, b, m) {
            var c = "{", h = !1, d = [], x = /f$/, e = /\.([0-9])/,
                g = f.defaultOptions.lang, R = m && m.time || f.time;
            for (m = m && m.numberFormatter || T; a;) {
                var r = a.indexOf(c);
                if (-1 === r) break;
                var w = a.slice(0, r);
                if (h) {
                    w = w.split(":");
                    c = G(w.shift() || "", b);
                    if (w.length && "number" === typeof c) if (w = w.join(":"), x.test(w)) {
                        var O = parseInt((w.match(e) || ["", "-1"])[1], 10);
                        null !== c && (c = m(c, O, g.decimalPoint, -1 < w.indexOf(",") ? g.thousandsSep : ""))
                    } else c = R.dateFormat(w, c);
                    d.push(c)
                } else d.push(w);
                a = a.slice(r + 1);
                c = (h = !h) ? "}" : "{"
            }
            d.push(a);
            return d.join("")
        }, J = f.getMagnitude = function (a) {
            return Math.pow(10,
                Math.floor(Math.log(a) / Math.LN10))
        }, N = f.normalizeTickInterval = function (a, b, m, c, h) {
            var d = a;
            m = q(m, 1);
            var x = a / m;
            b || (b = h ? [1, 1.2, 1.5, 2, 2.5, 3, 4, 5, 6, 8, 10] : [1, 2, 2.5, 5, 10], !1 === c && (1 === m ? b = b.filter(function (a) {
                return 0 === a % 1
            }) : .1 >= m && (b = [1 / m])));
            for (c = 0; c < b.length && !(d = b[c], h && d * m >= a || !h && x <= (b[c] + (b[c + 1] || b[c])) / 2); c++) ;
            return d = U(d * m, -Math.round(Math.log(.001) / Math.LN10))
        }, x = f.stableSort = function (a, b) {
            var m = a.length, c, h;
            for (h = 0; h < m; h++) a[h].safeI = h;
            a.sort(function (a, m) {
                c = b(a, m);
                return 0 === c ? a.safeI - m.safeI :
                    c
            });
            for (h = 0; h < m; h++) delete a[h].safeI
        }, m = f.arrayMin = function (a) {
            for (var b = a.length, m = a[0]; b--;) a[b] < m && (m = a[b]);
            return m
        }, K = f.arrayMax = function (a) {
            for (var b = a.length, m = a[0]; b--;) a[b] > m && (m = a[b]);
            return m
        }, O = f.destroyObjectProperties = function (a, b) {
            W(a, function (m, c) {
                m && m !== b && m.destroy && m.destroy();
                delete a[c]
            })
        }, V = f.discardElement = function (a) {
            var b = f.garbageBin;
            b || (b = z("div"));
            a && b.appendChild(a);
            b.innerHTML = ""
        }, U = f.correctFloat = function (a, b) {
            return parseFloat(a.toPrecision(b || 14))
        }, X = f.setAnimation =
            function (a, b) {
                b.renderer.globalAnimation = q(a, b.options.chart.animation, !0)
            }, Q = f.animObject = function (a) {
            return y(a) ? k(a) : {duration: a ? 500 : 0}
        }, R = f.timeUnits = {
            millisecond: 1,
            second: 1E3,
            minute: 6E4,
            hour: 36E5,
            day: 864E5,
            week: 6048E5,
            month: 24192E5,
            year: 314496E5
        }, T = f.numberFormat = function (a, b, m, c) {
            a = +a || 0;
            b = +b;
            var h = f.defaultOptions.lang, d = (a.toString().split(".")[1] || "").split("e")[0].length,
                x = a.toString().split("e");
            if (-1 === b) b = Math.min(d, 20); else if (!n(b)) b = 2; else if (b && x[1] && 0 > x[1]) {
                var e = b + +x[1];
                0 <= e ? (x[0] =
                    (+x[0]).toExponential(e).split("e")[0], b = e) : (x[0] = x[0].split(".")[0] || 0, a = 20 > b ? (x[0] * Math.pow(10, x[1])).toFixed(b) : 0, x[1] = 0)
            }
            var g = (Math.abs(x[1] ? x[0] : a) + Math.pow(10, -Math.max(b, d) - 1)).toFixed(b);
            d = String(A(g));
            e = 3 < d.length ? d.length % 3 : 0;
            m = q(m, h.decimalPoint);
            c = q(c, h.thousandsSep);
            a = (0 > a ? "-" : "") + (e ? d.substr(0, e) + c : "");
            a += d.substr(e).replace(/(\d{3})(?=\d)/g, "$1" + c);
            b && (a += m + g.slice(-b));
            x[1] && 0 !== +a && (a += "e" + x[1]);
            return a
        };
        Math.easeInOutSine = function (a) {
            return -.5 * (Math.cos(Math.PI * a) - 1)
        };
        var ea = f.getStyle =
                function (a, b, m) {
                    if ("width" === b) return b = Math.min(a.offsetWidth, a.scrollWidth), m = a.getBoundingClientRect && a.getBoundingClientRect().width, m < b && m >= b - 1 && (b = Math.floor(m)), Math.max(0, b - f.getStyle(a, "padding-left") - f.getStyle(a, "padding-right"));
                    if ("height" === b) return Math.max(0, Math.min(a.offsetHeight, a.scrollHeight) - f.getStyle(a, "padding-top") - f.getStyle(a, "padding-bottom"));
                    C.getComputedStyle || F(27, !0);
                    if (a = C.getComputedStyle(a, void 0)) a = a.getPropertyValue(b), q(m, "opacity" !== b) && (a = A(a));
                    return a
                },
            Z = f.inArray = function (a, b, m) {
                return b.indexOf(a, m)
            }, S = f.find = Array.prototype.find ? function (a, b) {
                return a.find(b)
            } : function (a, b) {
                var m, c = a.length;
                for (m = 0; m < c; m++) if (b(a[m], m)) return a[m]
            };
        f.keys = Object.keys;
        var ba = f.offset = function (a) {
            var b = L.documentElement;
            a = a.parentElement || a.parentNode ? a.getBoundingClientRect() : {top: 0, left: 0};
            return {
                top: a.top + (C.pageYOffset || b.scrollTop) - (b.clientTop || 0),
                left: a.left + (C.pageXOffset || b.scrollLeft) - (b.clientLeft || 0)
            }
        }, ca = f.stop = function (a, b) {
            for (var m = f.timers.length; m--;) f.timers[m].elem !==
            a || b && b !== f.timers[m].prop || (f.timers[m].stopped = !0)
        }, W = f.objectEach = function (a, b, m) {
            for (var c in a) Object.hasOwnProperty.call(a, c) && b.call(m || a[c], a[c], c, a)
        };
        W({map: "map", each: "forEach", grep: "filter", reduce: "reduce", some: "some"}, function (a, b) {
            f[b] = function (b) {
                return Array.prototype[a].apply(b, [].slice.call(arguments, 1))
            }
        });
        var aa = f.addEvent = function (a, b, m, c) {
            void 0 === c && (c = {});
            var h = a.addEventListener || f.addEventListenerPolyfill;
            var d = "function" === typeof a && a.prototype ? a.prototype.protoEvents = a.prototype.protoEvents ||
                {} : a.hcEvents = a.hcEvents || {};
            f.Point && a instanceof f.Point && a.series && a.series.chart && (a.series.chart.runTrackerClick = !0);
            h && h.call(a, b, m, !1);
            d[b] || (d[b] = []);
            d[b].push({fn: m, order: "number" === typeof c.order ? c.order : Infinity});
            d[b].sort(function (a, b) {
                return a.order - b.order
            });
            return function () {
                da(a, b, m)
            }
        }, da = f.removeEvent = function (a, b, m) {
            function c(b, m) {
                var c = a.removeEventListener || f.removeEventListenerPolyfill;
                c && c.call(a, b, m, !1)
            }

            function h(m) {
                var h;
                if (a.nodeName) {
                    if (b) {
                        var d = {};
                        d[b] = !0
                    } else d = m;
                    W(d,
                        function (a, b) {
                            if (m[b]) for (h = m[b].length; h--;) c(b, m[b][h].fn)
                        })
                }
            }

            var d;
            ["protoEvents", "hcEvents"].forEach(function (x, e) {
                var g = (e = e ? a : a.prototype) && e[x];
                g && (b ? (d = g[b] || [], m ? (g[b] = d.filter(function (a) {
                    return m !== a.fn
                }), c(b, m)) : (h(g), g[b] = [])) : (h(g), e[x] = {}))
            })
        }, ha = f.fireEvent = function (b, m, c, h) {
            var d;
            c = c || {};
            if (L.createEvent && (b.dispatchEvent || b.fireEvent)) {
                var x = L.createEvent("Events");
                x.initEvent(m, !0, !0);
                a(x, c);
                b.dispatchEvent ? b.dispatchEvent(x) : b.fireEvent(m, x)
            } else c.target || a(c, {
                preventDefault: function () {
                    c.defaultPrevented =
                        !0
                }, target: b, type: m
            }), function (a, m) {
                void 0 === a && (a = []);
                void 0 === m && (m = []);
                var h = 0, x = 0, e = a.length + m.length;
                for (d = 0; d < e; d++) !1 === (a[h] ? m[x] ? a[h].order <= m[x].order ? a[h++] : m[x++] : a[h++] : m[x++]).fn.call(b, c) && c.preventDefault()
            }(b.protoEvents && b.protoEvents[m], b.hcEvents && b.hcEvents[m]);
            h && !c.defaultPrevented && h.call(b, c)
        }, ia = f.animate = function (a, b, m) {
            var c, h = "", d, x;
            if (!y(m)) {
                var e = arguments;
                m = {duration: e[2], easing: e[3], complete: e[4]}
            }
            n(m.duration) || (m.duration = 400);
            m.easing = "function" === typeof m.easing ?
                m.easing : Math[m.easing] || Math.easeInOutSine;
            m.curAnim = k(b);
            W(b, function (e, g) {
                ca(a, g);
                x = new E(a, m, g);
                d = null;
                "d" === g ? (x.paths = x.initPath(a, a.d, b.d), x.toD = b.d, c = 0, d = 1) : a.attr ? c = a.attr(g) : (c = parseFloat(ea(a, g)) || 0, "opacity" !== g && (h = "px"));
                d || (d = e);
                d && d.match && d.match("px") && (d = d.replace(/px/g, ""));
                x.run(c, d, h)
            })
        }, u = f.seriesType = function (a, b, m, c, h) {
            var d = f.getOptions(), x = f.seriesTypes;
            d.plotOptions[a] = k(d.plotOptions[b], m);
            x[a] = D(x[b] || function () {
            }, c);
            x[a].prototype.type = a;
            h && (x[a].prototype.pointClass =
                D(f.Point, h));
            return x[a]
        }, fa = f.uniqueKey = function () {
            var a = Math.random().toString(36).substring(2, 9), b = 0;
            return function () {
                return "highcharts-" + a + "-" + b++
            }
        }(), ja = f.isFunction = function (a) {
            return "function" === typeof a
        };
        C.jQuery && (C.jQuery.fn.highcharts = function () {
            var a = [].slice.call(arguments);
            if (this[0]) return a[0] ? (new (f[v(a[0]) ? a.shift() : "Chart"])(this[0], a[0], a[1]), this) : M[H(this[0], "data-highcharts-chart")]
        });
        return {
            Fx: E,
            addEvent: aa,
            animate: ia,
            animObject: Q,
            arrayMax: K,
            arrayMin: m,
            attr: H,
            clamp: function (a,
                             b, m) {
                return a > b ? a < m ? a : m : b
            },
            clearTimeout: b,
            correctFloat: U,
            createElement: z,
            css: w,
            defined: d,
            destroyObjectProperties: O,
            discardElement: V,
            erase: e,
            error: F,
            extend: a,
            extendClass: D,
            find: S,
            fireEvent: ha,
            format: I,
            getMagnitude: J,
            getNestedProperty: G,
            getStyle: ea,
            inArray: Z,
            isArray: B,
            isClass: l,
            isDOMElement: p,
            isFunction: ja,
            isNumber: n,
            isObject: y,
            isString: v,
            merge: k,
            normalizeTickInterval: N,
            numberFormat: T,
            objectEach: W,
            offset: ba,
            pad: r,
            pick: q,
            pInt: A,
            relativeLength: h,
            removeEvent: da,
            seriesType: u,
            setAnimation: X,
            splat: g,
            stableSort: x,
            stop: ca,
            syncTimeout: c,
            timeUnits: R,
            uniqueKey: fa,
            wrap: t
        }
    });
    P(u, "parts/Color.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.isNumber, q = k.merge, G = k.pInt;
        k = function () {
            function f(k) {
                this.parsers = [{
                    regex: /rgba\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]?(?:\.[0-9]+)?)\s*\)/,
                    parse: function (f) {
                        return [G(f[1]), G(f[2]), G(f[3]), parseFloat(f[4], 10)]
                    }
                }, {
                    regex: /rgb\(\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*,\s*([0-9]{1,3})\s*\)/, parse: function (f) {
                        return [G(f[1]), G(f[2]),
                            G(f[3]), 1]
                    }
                }];
                this.rgba = [];
                if (!(this instanceof f)) return new f(k);
                this.init(k)
            }

            f.parse = function (k) {
                return new f(k)
            };
            f.prototype.init = function (k) {
                var C, q;
                if ((this.input = k = f.names[k && k.toLowerCase ? k.toLowerCase() : ""] || k) && k.stops) this.stops = k.stops.map(function (v) {
                    return new f(v[1])
                }); else {
                    if (k && k.charAt && "#" === k.charAt()) {
                        var E = k.length;
                        k = parseInt(k.substr(1), 16);
                        7 === E ? C = [(k & 16711680) >> 16, (k & 65280) >> 8, k & 255, 1] : 4 === E && (C = [(k & 3840) >> 4 | (k & 3840) >> 8, (k & 240) >> 4 | k & 240, (k & 15) << 4 | k & 15, 1])
                    }
                    if (!C) for (q = this.parsers.length; q-- &&
                    !C;) {
                        var A = this.parsers[q];
                        (E = A.regex.exec(k)) && (C = A.parse(E))
                    }
                }
                this.rgba = C || []
            };
            f.prototype.get = function (f) {
                var k = this.input, F = this.rgba;
                if ("undefined" !== typeof this.stops) {
                    var E = q(k);
                    E.stops = [].concat(E.stops);
                    this.stops.forEach(function (A, v) {
                        E.stops[v] = [E.stops[v][0], A.get(f)]
                    })
                } else E = F && H(F[0]) ? "rgb" === f || !f && 1 === F[3] ? "rgb(" + F[0] + "," + F[1] + "," + F[2] + ")" : "a" === f ? F[3] : "rgba(" + F.join(",") + ")" : k;
                return E
            };
            f.prototype.brighten = function (f) {
                var k, q = this.rgba;
                if (this.stops) this.stops.forEach(function (k) {
                    k.brighten(f)
                });
                else if (H(f) && 0 !== f) for (k = 0; 3 > k; k++) q[k] += G(255 * f), 0 > q[k] && (q[k] = 0), 255 < q[k] && (q[k] = 255);
                return this
            };
            f.prototype.setOpacity = function (f) {
                this.rgba[3] = f;
                return this
            };
            f.prototype.tweenTo = function (f, k) {
                var q = this.rgba, E = f.rgba;
                E.length && q && q.length ? (f = 1 !== E[3] || 1 !== q[3], k = (f ? "rgba(" : "rgb(") + Math.round(E[0] + (q[0] - E[0]) * (1 - k)) + "," + Math.round(E[1] + (q[1] - E[1]) * (1 - k)) + "," + Math.round(E[2] + (q[2] - E[2]) * (1 - k)) + (f ? "," + (E[3] + (q[3] - E[3]) * (1 - k)) : "") + ")") : k = f.input || "none";
                return k
            };
            f.names = {white: "#ffffff", black: "#000000"};
            return f
        }();
        f.Color = k;
        f.color = k.parse;
        return f.Color
    });
    P(u, "parts/SvgRenderer.js", [u["parts/Globals.js"], u["parts/Color.js"], u["parts/Utilities.js"]], function (f, k, H) {
        var q = k.parse, G = H.addEvent, M = H.animate, L = H.animObject, C = H.attr, F = H.createElement, E = H.css,
            A = H.defined, v = H.destroyObjectProperties, B = H.erase, y = H.extend, p = H.inArray, l = H.isArray,
            n = H.isNumber, e = H.isObject, d = H.isString, g = H.merge, c = H.objectEach, b = H.pick, a = H.pInt,
            w = H.removeEvent, z = H.splat, D = H.stop, r = H.uniqueKey, h = f.charts, t = f.deg2rad, I = f.doc,
            J = f.hasTouch, N = f.isFirefox, x = f.isMS, m = f.isWebKit, K = f.noop, O = f.svg, V = f.SVG_NS,
            U = f.symbolSizes, X = f.win;
        var Q = f.SVGElement = function () {
            return this
        };
        y(Q.prototype, {
            opacity: 1,
            SVG_NS: V,
            textProps: "direction fontSize fontWeight fontFamily fontStyle color lineHeight width textAlign textDecoration textOverflow textOutline cursor".split(" "),
            init: function (a, b) {
                this.element = "span" === b ? F(b) : I.createElementNS(this.SVG_NS, b);
                this.renderer = a;
                f.fireEvent(this, "afterInit")
            },
            animate: function (a, m, h) {
                var d = L(b(m, this.renderer.globalAnimation,
                    !0));
                b(I.hidden, I.msHidden, I.webkitHidden, !1) && (d.duration = 0);
                0 !== d.duration ? (h && (d.complete = h), M(this, a, d)) : (this.attr(a, void 0, h), c(a, function (a, b) {
                    d.step && d.step.call(this, a, {prop: b, pos: 1})
                }, this));
                return this
            },
            complexColor: function (a, b, m) {
                var h = this.renderer, d, x, e, R, w, O, K, t, z, T, D, I = [], n;
                f.fireEvent(this.renderer, "complexColor", {args: arguments}, function () {
                    a.radialGradient ? x = "radialGradient" : a.linearGradient && (x = "linearGradient");
                    x && (e = a[x], w = h.gradients, K = a.stops, T = m.radialReference, l(e) && (a[x] =
                        e = {
                            x1: e[0],
                            y1: e[1],
                            x2: e[2],
                            y2: e[3],
                            gradientUnits: "userSpaceOnUse"
                        }), "radialGradient" === x && T && !A(e.gradientUnits) && (R = e, e = g(e, h.getRadialAttr(T, R), {gradientUnits: "userSpaceOnUse"})), c(e, function (a, b) {
                        "id" !== b && I.push(b, a)
                    }), c(K, function (a) {
                        I.push(a)
                    }), I = I.join(","), w[I] ? D = w[I].attr("id") : (e.id = D = r(), w[I] = O = h.createElement(x).attr(e).add(h.defs), O.radAttr = R, O.stops = [], K.forEach(function (a) {
                        0 === a[1].indexOf("rgba") ? (d = q(a[1]), t = d.get("rgb"), z = d.get("a")) : (t = a[1], z = 1);
                        a = h.createElement("stop").attr({
                            offset: a[0],
                            "stop-color": t, "stop-opacity": z
                        }).add(O);
                        O.stops.push(a)
                    })), n = "url(" + h.url + "#" + D + ")", m.setAttribute(b, n), m.gradient = I, a.toString = function () {
                        return n
                    })
                })
            },
            applyTextOutline: function (a) {
                var b = this.element, m;
                -1 !== a.indexOf("contrast") && (a = a.replace(/contrast/g, this.renderer.getContrast(b.style.fill)));
                a = a.split(" ");
                var c = a[a.length - 1];
                if ((m = a[0]) && "none" !== m && f.svg) {
                    this.fakeTS = !0;
                    a = [].slice.call(b.getElementsByTagName("tspan"));
                    this.ySetter = this.xSetter;
                    m = m.replace(/(^[\d\.]+)(.*?)$/g, function (a, b,
                                                                 m) {
                        return 2 * b + m
                    });
                    this.removeTextOutline(a);
                    var h = b.textContent ? /^[\u0591-\u065F\u066A-\u07FF\uFB1D-\uFDFD\uFE70-\uFEFC]/.test(b.textContent) : !1;
                    var d = b.firstChild;
                    a.forEach(function (a, x) {
                        0 === x && (a.setAttribute("x", b.getAttribute("x")), x = b.getAttribute("y"), a.setAttribute("y", x || 0), null === x && b.setAttribute("y", 0));
                        x = a.cloneNode(!0);
                        C(h && !N ? a : x, {
                            "class": "highcharts-text-outline",
                            fill: c,
                            stroke: c,
                            "stroke-width": m,
                            "stroke-linejoin": "round"
                        });
                        b.insertBefore(x, d)
                    });
                    h && N && a[0] && (a = a[0].cloneNode(!0), a.textContent =
                        " ", b.insertBefore(a, d))
                }
            },
            removeTextOutline: function (a) {
                for (var b = a.length, m; b--;) m = a[b], "highcharts-text-outline" === m.getAttribute("class") && B(a, this.element.removeChild(m))
            },
            symbolCustomAttribs: "x y width height r start end innerR anchorX anchorY rounded".split(" "),
            attr: function (a, b, m, h) {
                var d = this.element, x, e = this, g, w, r = this.symbolCustomAttribs;
                if ("string" === typeof a && "undefined" !== typeof b) {
                    var R = a;
                    a = {};
                    a[R] = b
                }
                "string" === typeof a ? e = (this[a + "Getter"] || this._defaultGetter).call(this, a, d) : (c(a,
                    function (b, m) {
                        g = !1;
                        h || D(this, m);
                        this.symbolName && -1 !== p(m, r) && (x || (this.symbolAttr(a), x = !0), g = !0);
                        !this.rotation || "x" !== m && "y" !== m || (this.doTransform = !0);
                        g || (w = this[m + "Setter"] || this._defaultSetter, w.call(this, b, m, d), !this.styledMode && this.shadows && /^(width|height|visibility|x|y|d|transform|cx|cy|r)$/.test(m) && this.updateShadows(m, b, w))
                    }, this), this.afterSetters());
                m && m.call(this);
                return e
            },
            afterSetters: function () {
                this.doTransform && (this.updateTransform(), this.doTransform = !1)
            },
            updateShadows: function (a,
                                     b, m) {
                for (var c = this.shadows, h = c.length; h--;) m.call(c[h], "height" === a ? Math.max(b - (c[h].cutHeight || 0), 0) : "d" === a ? this.d : b, a, c[h])
            },
            addClass: function (a, b) {
                var m = b ? "" : this.attr("class") || "";
                a = (a || "").split(/ /g).reduce(function (a, b) {
                    -1 === m.indexOf(b) && a.push(b);
                    return a
                }, m ? [m] : []).join(" ");
                a !== m && this.attr("class", a);
                return this
            },
            hasClass: function (a) {
                return -1 !== (this.attr("class") || "").split(" ").indexOf(a)
            },
            removeClass: function (a) {
                return this.attr("class", (this.attr("class") || "").replace(d(a) ? new RegExp(" ?" +
                    a + " ?") : a, ""))
            },
            symbolAttr: function (a) {
                var m = this;
                "x y r start end width height innerR anchorX anchorY clockwise".split(" ").forEach(function (c) {
                    m[c] = b(a[c], m[c])
                });
                m.attr({d: m.renderer.symbols[m.symbolName](m.x, m.y, m.width, m.height, m)})
            },
            clip: function (a) {
                return this.attr("clip-path", a ? "url(" + this.renderer.url + "#" + a.id + ")" : "none")
            },
            crisp: function (a, b) {
                b = b || a.strokeWidth || 0;
                var m = Math.round(b) % 2 / 2;
                a.x = Math.floor(a.x || this.x || 0) + m;
                a.y = Math.floor(a.y || this.y || 0) + m;
                a.width = Math.floor((a.width || this.width ||
                    0) - 2 * m);
                a.height = Math.floor((a.height || this.height || 0) - 2 * m);
                A(a.strokeWidth) && (a.strokeWidth = b);
                return a
            },
            css: function (b) {
                var m = this.styles, h = {}, d = this.element, x = "", e = !m,
                    g = ["textOutline", "textOverflow", "width"];
                b && b.color && (b.fill = b.color);
                m && c(b, function (a, b) {
                    a !== m[b] && (h[b] = a, e = !0)
                });
                if (e) {
                    m && (b = y(m, h));
                    if (b) if (null === b.width || "auto" === b.width) delete this.textWidth; else if ("text" === d.nodeName.toLowerCase() && b.width) var w = this.textWidth = a(b.width);
                    this.styles = b;
                    w && !O && this.renderer.forExport && delete b.width;
                    if (d.namespaceURI === this.SVG_NS) {
                        var r = function (a, b) {
                            return "-" + b.toLowerCase()
                        };
                        c(b, function (a, b) {
                            -1 === g.indexOf(b) && (x += b.replace(/([A-Z])/g, r) + ":" + a + ";")
                        });
                        x && C(d, "style", x)
                    } else E(d, b);
                    this.added && ("text" === this.element.nodeName && this.renderer.buildText(this), b && b.textOutline && this.applyTextOutline(b.textOutline))
                }
                return this
            },
            getStyle: function (a) {
                return X.getComputedStyle(this.element || this, "").getPropertyValue(a)
            },
            strokeWidth: function () {
                if (!this.renderer.styledMode) return this["stroke-width"] ||
                    0;
                var b = this.getStyle("stroke-width"), m = 0;
                if (b.indexOf("px") === b.length - 2) m = a(b); else if ("" !== b) {
                    var c = I.createElementNS(V, "rect");
                    C(c, {width: b, "stroke-width": 0});
                    this.element.parentNode.appendChild(c);
                    m = c.getBBox().width;
                    c.parentNode.removeChild(c)
                }
                return m
            },
            on: function (a, b) {
                var m, c, h = this.element, d;
                J && "click" === a ? (h.ontouchstart = function (a) {
                    m = a.touches[0].clientX;
                    c = a.touches[0].clientY
                }, h.ontouchend = function (a) {
                    m && 4 <= Math.sqrt(Math.pow(m - a.changedTouches[0].clientX, 2) + Math.pow(c - a.changedTouches[0].clientY,
                        2)) || b.call(h, a);
                    d = !0;
                    a.preventDefault()
                }, h.onclick = function (a) {
                    d || b.call(h, a)
                }) : h["on" + a] = b;
                return this
            },
            setRadialReference: function (a) {
                var b = this.renderer.gradients[this.element.gradient];
                this.element.radialReference = a;
                b && b.radAttr && b.animate(this.renderer.getRadialAttr(a, b.radAttr));
                return this
            },
            translate: function (a, b) {
                return this.attr({translateX: a, translateY: b})
            },
            invert: function (a) {
                this.inverted = a;
                this.updateTransform();
                return this
            },
            updateTransform: function () {
                var a = this.translateX || 0, m = this.translateY ||
                    0, c = this.scaleX, h = this.scaleY, d = this.inverted, x = this.rotation, e = this.matrix,
                    g = this.element;
                d && (a += this.width, m += this.height);
                a = ["translate(" + a + "," + m + ")"];
                A(e) && a.push("matrix(" + e.join(",") + ")");
                d ? a.push("rotate(90) scale(-1,1)") : x && a.push("rotate(" + x + " " + b(this.rotationOriginX, g.getAttribute("x"), 0) + " " + b(this.rotationOriginY, g.getAttribute("y") || 0) + ")");
                (A(c) || A(h)) && a.push("scale(" + b(c, 1) + " " + b(h, 1) + ")");
                a.length && g.setAttribute("transform", a.join(" "))
            },
            toFront: function () {
                var a = this.element;
                a.parentNode.appendChild(a);
                return this
            },
            align: function (a, m, c) {
                var h, x = {};
                var e = this.renderer;
                var g = e.alignedObjects;
                var w, r;
                if (a) {
                    if (this.alignOptions = a, this.alignByTranslate = m, !c || d(c)) this.alignTo = h = c || "renderer", B(g, this), g.push(this), c = null
                } else a = this.alignOptions, m = this.alignByTranslate, h = this.alignTo;
                c = b(c, e[h], e);
                h = a.align;
                e = a.verticalAlign;
                g = (c.x || 0) + (a.x || 0);
                var O = (c.y || 0) + (a.y || 0);
                "right" === h ? w = 1 : "center" === h && (w = 2);
                w && (g += (c.width - (a.width || 0)) / w);
                x[m ? "translateX" : "x"] = Math.round(g);
                "bottom" === e ? r = 1 : "middle" ===
                    e && (r = 2);
                r && (O += (c.height - (a.height || 0)) / r);
                x[m ? "translateY" : "y"] = Math.round(O);
                this[this.placed ? "animate" : "attr"](x);
                this.placed = !0;
                this.alignAttr = x;
                return this
            },
            getBBox: function (a, m) {
                var c, h = this.renderer, d = this.element, x = this.styles, e = this.textStr, g, w = h.cache,
                    r = h.cacheKeys, O = d.namespaceURI === this.SVG_NS;
                m = b(m, this.rotation, 0);
                var K = h.styledMode ? d && Q.prototype.getStyle.call(d, "font-size") : x && x.fontSize;
                if (A(e)) {
                    var z = e.toString();
                    -1 === z.indexOf("<") && (z = z.replace(/[0-9]/g, "0"));
                    z += ["", m, K, this.textWidth,
                        x && x.textOverflow].join()
                }
                z && !a && (c = w[z]);
                if (!c) {
                    if (O || h.forExport) {
                        try {
                            (g = this.fakeTS && function (a) {
                                [].forEach.call(d.querySelectorAll(".highcharts-text-outline"), function (b) {
                                    b.style.display = a
                                })
                            }) && g("none"), c = d.getBBox ? y({}, d.getBBox()) : {
                                width: d.offsetWidth,
                                height: d.offsetHeight
                            }, g && g("")
                        } catch (fa) {
                            ""
                        }
                        if (!c || 0 > c.width) c = {width: 0, height: 0}
                    } else c = this.htmlGetBBox();
                    h.isSVG && (a = c.width, h = c.height, O && (c.height = h = {
                        "11px,17": 14,
                        "13px,20": 16
                    }[x && x.fontSize + "," + Math.round(h)] || h), m && (x = m * t, c.width = Math.abs(h *
                        Math.sin(x)) + Math.abs(a * Math.cos(x)), c.height = Math.abs(h * Math.cos(x)) + Math.abs(a * Math.sin(x))));
                    if (z && 0 < c.height) {
                        for (; 250 < r.length;) delete w[r.shift()];
                        w[z] || r.push(z);
                        w[z] = c
                    }
                }
                return c
            },
            show: function (a) {
                return this.attr({visibility: a ? "inherit" : "visible"})
            },
            hide: function (a) {
                a ? this.attr({y: -9999}) : this.attr({visibility: "hidden"});
                return this
            },
            fadeOut: function (a) {
                var b = this;
                b.animate({opacity: 0}, {
                    duration: a || 150, complete: function () {
                        b.attr({y: -9999})
                    }
                })
            },
            add: function (a) {
                var b = this.renderer, m = this.element;
                a && (this.parentGroup = a);
                this.parentInverted = a && a.inverted;
                "undefined" !== typeof this.textStr && b.buildText(this);
                this.added = !0;
                if (!a || a.handleZ || this.zIndex) var c = this.zIndexSetter();
                c || (a ? a.element : b.box).appendChild(m);
                if (this.onAdd) this.onAdd();
                return this
            },
            safeRemoveChild: function (a) {
                var b = a.parentNode;
                b && b.removeChild(a)
            },
            destroy: function () {
                var a = this, b = a.element || {}, m = a.renderer,
                    h = m.isSVG && "SPAN" === b.nodeName && a.parentGroup, d = b.ownerSVGElement, x = a.clipPath;
                b.onclick = b.onmouseout = b.onmouseover =
                    b.onmousemove = b.point = null;
                D(a);
                x && d && ([].forEach.call(d.querySelectorAll("[clip-path],[CLIP-PATH]"), function (a) {
                    -1 < a.getAttribute("clip-path").indexOf(x.element.id) && a.removeAttribute("clip-path")
                }), a.clipPath = x.destroy());
                if (a.stops) {
                    for (d = 0; d < a.stops.length; d++) a.stops[d] = a.stops[d].destroy();
                    a.stops = null
                }
                a.safeRemoveChild(b);
                for (m.styledMode || a.destroyShadows(); h && h.div && 0 === h.div.childNodes.length;) b = h.parentGroup, a.safeRemoveChild(h.div), delete h.div, h = b;
                a.alignTo && B(m.alignedObjects, a);
                c(a,
                    function (b, m) {
                        a[m] && a[m].parentGroup === a && a[m].destroy && a[m].destroy();
                        delete a[m]
                    })
            },
            shadow: function (a, m, c) {
                var h = [], d, x = this.element;
                if (!a) this.destroyShadows(); else if (!this.shadows) {
                    var e = b(a.width, 3);
                    var g = (a.opacity || .15) / e;
                    var w = this.parentInverted ? "(-1,-1)" : "(" + b(a.offsetX, 1) + ", " + b(a.offsetY, 1) + ")";
                    for (d = 1; d <= e; d++) {
                        var r = x.cloneNode(0);
                        var O = 2 * e + 1 - 2 * d;
                        C(r, {
                            stroke: a.color || "#000000",
                            "stroke-opacity": g * d,
                            "stroke-width": O,
                            transform: "translate" + w,
                            fill: "none"
                        });
                        r.setAttribute("class", (r.getAttribute("class") ||
                            "") + " highcharts-shadow");
                        c && (C(r, "height", Math.max(C(r, "height") - O, 0)), r.cutHeight = O);
                        m ? m.element.appendChild(r) : x.parentNode && x.parentNode.insertBefore(r, x);
                        h.push(r)
                    }
                    this.shadows = h
                }
                return this
            },
            destroyShadows: function () {
                (this.shadows || []).forEach(function (a) {
                    this.safeRemoveChild(a)
                }, this);
                this.shadows = void 0
            },
            xGetter: function (a) {
                "circle" === this.element.nodeName && ("x" === a ? a = "cx" : "y" === a && (a = "cy"));
                return this._defaultGetter(a)
            },
            _defaultGetter: function (a) {
                a = b(this[a + "Value"], this[a], this.element ?
                    this.element.getAttribute(a) : null, 0);
                /^[\-0-9\.]+$/.test(a) && (a = parseFloat(a));
                return a
            },
            dSetter: function (a, b, m) {
                a && a.join && (a = a.join(" "));
                /(NaN| {2}|^$)/.test(a) && (a = "M 0 0");
                this[b] !== a && (m.setAttribute(b, a), this[b] = a)
            },
            dashstyleSetter: function (b) {
                var m, c = this["stroke-width"];
                "inherit" === c && (c = 1);
                if (b = b && b.toLowerCase()) {
                    b = b.replace("shortdashdotdot", "3,1,1,1,1,1,").replace("shortdashdot", "3,1,1,1").replace("shortdot", "1,1,").replace("shortdash", "3,1,").replace("longdash", "8,3,").replace(/dot/g,
                        "1,3,").replace("dash", "4,3,").replace(/,$/, "").split(",");
                    for (m = b.length; m--;) b[m] = a(b[m]) * c;
                    b = b.join(",").replace(/NaN/g, "none");
                    this.element.setAttribute("stroke-dasharray", b)
                }
            },
            alignSetter: function (a) {
                var b = {left: "start", center: "middle", right: "end"};
                b[a] && (this.alignValue = a, this.element.setAttribute("text-anchor", b[a]))
            },
            opacitySetter: function (a, b, m) {
                this[b] = a;
                m.setAttribute(b, a)
            },
            titleSetter: function (a) {
                var m = this.element.getElementsByTagName("title")[0];
                m || (m = I.createElementNS(this.SVG_NS, "title"),
                    this.element.appendChild(m));
                m.firstChild && m.removeChild(m.firstChild);
                m.appendChild(I.createTextNode(String(b(a, "")).replace(/<[^>]*>/g, "").replace(/&lt;/g, "<").replace(/&gt;/g, ">")))
            },
            textSetter: function (a) {
                a !== this.textStr && (delete this.bBox, delete this.textPxLength, this.textStr = a, this.added && this.renderer.buildText(this))
            },
            setTextPath: function (a, b) {
                var m = this.element, h = {textAnchor: "text-anchor"}, d = !1, x = this.textPathWrapper, e = !x;
                b = g(!0, {enabled: !0, attributes: {dy: -5, startOffset: "50%", textAnchor: "middle"}},
                    b);
                var w = b.attributes;
                if (a && b && b.enabled) {
                    x && null === x.element.parentNode ? (e = !0, x = x.destroy()) : x && this.removeTextOutline.call(x.parentGroup, [].slice.call(m.getElementsByTagName("tspan")));
                    this.options && this.options.padding && (w.dx = -this.options.padding);
                    x || (this.textPathWrapper = x = this.renderer.createElement("textPath"), d = !0);
                    var O = x.element;
                    (b = a.element.getAttribute("id")) || a.element.setAttribute("id", b = r());
                    if (e) for (a = m.getElementsByTagName("tspan"); a.length;) a[0].setAttribute("y", 0), n(w.dx) && a[0].setAttribute("x",
                        -w.dx), O.appendChild(a[0]);
                    d && x.add({element: this.text ? this.text.element : m});
                    O.setAttributeNS("http://www.w3.org/1999/xlink", "href", this.renderer.url + "#" + b);
                    A(w.dy) && (O.parentNode.setAttribute("dy", w.dy), delete w.dy);
                    A(w.dx) && (O.parentNode.setAttribute("dx", w.dx), delete w.dx);
                    c(w, function (a, b) {
                        O.setAttribute(h[b] || b, a)
                    });
                    m.removeAttribute("transform");
                    this.removeTextOutline.call(x, [].slice.call(m.getElementsByTagName("tspan")));
                    this.text && !this.renderer.styledMode && this.attr({fill: "none", "stroke-width": 0});
                    this.applyTextOutline = this.updateTransform = K
                } else x && (delete this.updateTransform, delete this.applyTextOutline, this.destroyTextPath(m, a), this.updateTransform(), this.options.rotation && this.applyTextOutline(this.options.style.textOutline));
                return this
            },
            destroyTextPath: function (a, b) {
                var m = a.getElementsByTagName("text")[0];
                if (m) {
                    if (m.removeAttribute("dx"), m.removeAttribute("dy"), b.element.setAttribute("id", ""), m.getElementsByTagName("textPath").length) {
                        for (a = this.textPathWrapper.element.childNodes; a.length;) m.appendChild(a[0]);
                        m.removeChild(this.textPathWrapper.element)
                    }
                } else if (a.getAttribute("dx") || a.getAttribute("dy")) a.removeAttribute("dx"), a.removeAttribute("dy");
                this.textPathWrapper = this.textPathWrapper.destroy()
            },
            fillSetter: function (a, b, m) {
                "string" === typeof a ? m.setAttribute(b, a) : a && this.complexColor(a, b, m)
            },
            visibilitySetter: function (a, b, m) {
                "inherit" === a ? m.removeAttribute(b) : this[b] !== a && m.setAttribute(b, a);
                this[b] = a
            },
            zIndexSetter: function (b, m) {
                var c = this.renderer, h = this.parentGroup, d = (h || c).element || c.box, x = this.element,
                    e = !1;
                c = d === c.box;
                var g = this.added;
                var w;
                A(b) ? (x.setAttribute("data-z-index", b), b = +b, this[m] === b && (g = !1)) : A(this[m]) && x.removeAttribute("data-z-index");
                this[m] = b;
                if (g) {
                    (b = this.zIndex) && h && (h.handleZ = !0);
                    m = d.childNodes;
                    for (w = m.length - 1; 0 <= w && !e; w--) {
                        h = m[w];
                        g = h.getAttribute("data-z-index");
                        var r = !A(g);
                        if (h !== x) if (0 > b && r && !c && !w) d.insertBefore(x, m[w]), e = !0; else if (a(g) <= b || r && (!A(b) || 0 <= b)) d.insertBefore(x, m[w + 1] || null), e = !0
                    }
                    e || (d.insertBefore(x, m[c ? 3 : 0] || null), e = !0)
                }
                return e
            },
            _defaultSetter: function (a,
                                      b, m) {
                m.setAttribute(b, a)
            }
        });
        Q.prototype.yGetter = Q.prototype.xGetter;
        Q.prototype.translateXSetter = Q.prototype.translateYSetter = Q.prototype.rotationSetter = Q.prototype.verticalAlignSetter = Q.prototype.rotationOriginXSetter = Q.prototype.rotationOriginYSetter = Q.prototype.scaleXSetter = Q.prototype.scaleYSetter = Q.prototype.matrixSetter = function (a, b) {
            this[b] = a;
            this.doTransform = !0
        };
        Q.prototype["stroke-widthSetter"] = Q.prototype.strokeSetter = function (a, b, m) {
            this[b] = a;
            this.stroke && this["stroke-width"] ? (Q.prototype.fillSetter.call(this,
                this.stroke, "stroke", m), m.setAttribute("stroke-width", this["stroke-width"]), this.hasStroke = !0) : "stroke-width" === b && 0 === a && this.hasStroke ? (m.removeAttribute("stroke"), this.hasStroke = !1) : this.renderer.styledMode && this["stroke-width"] && (m.setAttribute("stroke-width", this["stroke-width"]), this.hasStroke = !0)
        };
        k = f.SVGRenderer = function () {
            this.init.apply(this, arguments)
        };
        y(k.prototype, {
            Element: Q, SVG_NS: V, init: function (a, b, c, h, d, x, e) {
                var g = this.createElement("svg").attr({version: "1.1", "class": "highcharts-root"});
                e || g.css(this.getStyle(h));
                h = g.element;
                a.appendChild(h);
                C(a, "dir", "ltr");
                -1 === a.innerHTML.indexOf("xmlns") && C(h, "xmlns", this.SVG_NS);
                this.isSVG = !0;
                this.box = h;
                this.boxWrapper = g;
                this.alignedObjects = [];
                this.url = (N || m) && I.getElementsByTagName("base").length ? X.location.href.split("#")[0].replace(/<[^>]*>/g, "").replace(/([\('\)])/g, "\\$1").replace(/ /g, "%20") : "";
                this.createElement("desc").add().element.appendChild(I.createTextNode("Created with Highcharts 8.0.4"));
                this.defs = this.createElement("defs").add();
                this.allowHTML = x;
                this.forExport = d;
                this.styledMode = e;
                this.gradients = {};
                this.cache = {};
                this.cacheKeys = [];
                this.imgCount = 0;
                this.setSize(b, c, !1);
                var w;
                N && a.getBoundingClientRect && (b = function () {
                    E(a, {left: 0, top: 0});
                    w = a.getBoundingClientRect();
                    E(a, {left: Math.ceil(w.left) - w.left + "px", top: Math.ceil(w.top) - w.top + "px"})
                }, b(), this.unSubPixelFix = G(X, "resize", b))
            }, definition: function (a) {
                function b(a, h) {
                    var d;
                    z(a).forEach(function (a) {
                        var x = m.createElement(a.tagName), e = {};
                        c(a, function (a, b) {
                            "tagName" !== b && "children" !==
                            b && "textContent" !== b && (e[b] = a)
                        });
                        x.attr(e);
                        x.add(h || m.defs);
                        a.textContent && x.element.appendChild(I.createTextNode(a.textContent));
                        b(a.children || [], x);
                        d = x
                    });
                    return d
                }

                var m = this;
                return b(a)
            }, getStyle: function (a) {
                return this.style = y({
                    fontFamily: '"Lucida Grande", "Lucida Sans Unicode", Arial, Helvetica, sans-serif',
                    fontSize: "12px"
                }, a)
            }, setStyle: function (a) {
                this.boxWrapper.css(this.getStyle(a))
            }, isHidden: function () {
                return !this.boxWrapper.getBBox().width
            }, destroy: function () {
                var a = this.defs;
                this.box = null;
                this.boxWrapper = this.boxWrapper.destroy();
                v(this.gradients || {});
                this.gradients = null;
                a && (this.defs = a.destroy());
                this.unSubPixelFix && this.unSubPixelFix();
                return this.alignedObjects = null
            }, createElement: function (a) {
                var b = new this.Element;
                b.init(this, a);
                return b
            }, draw: K, getRadialAttr: function (a, b) {
                return {cx: a[0] - a[2] / 2 + b.cx * a[2], cy: a[1] - a[2] / 2 + b.cy * a[2], r: b.r * a[2]}
            }, truncate: function (a, b, m, c, h, d, x) {
                var e = this, g = a.rotation, w, r = c ? 1 : 0, O = (m || c).length, K = O, t = [], z = function (a) {
                    b.firstChild && b.removeChild(b.firstChild);
                    a && b.appendChild(I.createTextNode(a))
                }, l = function (d, g) {
                    g = g || d;
                    if ("undefined" === typeof t[g]) if (b.getSubStringLength) try {
                        t[g] = h + b.getSubStringLength(0, c ? g + 1 : g)
                    } catch (ka) {
                        ""
                    } else e.getSpanWidth && (z(x(m || c, d)), t[g] = h + e.getSpanWidth(a, b));
                    return t[g]
                }, D;
                a.rotation = 0;
                var n = l(b.textContent.length);
                if (D = h + n > d) {
                    for (; r <= O;) K = Math.ceil((r + O) / 2), c && (w = x(c, K)), n = l(K, w && w.length - 1), r === O ? r = O + 1 : n > d ? O = K - 1 : r = K;
                    0 === O ? z("") : m && O === m.length - 1 || z(w || x(m || c, K))
                }
                c && c.splice(0, K);
                a.actualWidth = n;
                a.rotation = g;
                return D
            }, escapes: {
                "&": "&amp;",
                "<": "&lt;", ">": "&gt;", "'": "&#39;", '"': "&quot;"
            }, buildText: function (m) {
                var h = m.element, d = this, x = d.forExport, e = b(m.textStr, "").toString(),
                    g = -1 !== e.indexOf("<"), w = h.childNodes, r, K = C(h, "x"), t = m.styles, z = m.textWidth,
                    l = t && t.lineHeight, D = t && t.textOutline, n = t && "ellipsis" === t.textOverflow,
                    J = t && "nowrap" === t.whiteSpace, p = t && t.fontSize, U, N = w.length;
                t = z && !m.added && this.box;
                var f = function (b) {
                    var m;
                    d.styledMode || (m = /(px|em)$/.test(b && b.style.fontSize) ? b.style.fontSize : p || d.style.fontSize || 12);
                    return l ? a(l) : d.fontMetrics(m,
                        b.getAttribute("style") ? b : h).h
                }, R = function (a, b) {
                    c(d.escapes, function (m, c) {
                        b && -1 !== b.indexOf(m) || (a = a.toString().replace(new RegExp(m, "g"), c))
                    });
                    return a
                }, v = function (a, b) {
                    var m = a.indexOf("<");
                    a = a.substring(m, a.indexOf(">") - m);
                    m = a.indexOf(b + "=");
                    if (-1 !== m && (m = m + b.length + 1, b = a.charAt(m), '"' === b || "'" === b)) return a = a.substring(m + 1), a.substring(0, a.indexOf(b))
                }, y = /<br.*?>/g;
                var X = [e, n, J, l, D, p, z].join();
                if (X !== m.textCache) {
                    for (m.textCache = X; N--;) h.removeChild(w[N]);
                    g || D || n || z || -1 !== e.indexOf(" ") && (!J ||
                        y.test(e)) ? (t && t.appendChild(h), g ? (e = d.styledMode ? e.replace(/<(b|strong)>/g, '<span class="highcharts-strong">').replace(/<(i|em)>/g, '<span class="highcharts-emphasized">') : e.replace(/<(b|strong)>/g, '<span style="font-weight:bold">').replace(/<(i|em)>/g, '<span style="font-style:italic">'), e = e.replace(/<a/g, "<span").replace(/<\/(b|strong|i|em|a)>/g, "</span>").split(y)) : e = [e], e = e.filter(function (a) {
                        return "" !== a
                    }), e.forEach(function (a, b) {
                        var c = 0, e = 0;
                        a = a.replace(/^\s+|\s+$/g, "").replace(/<span/g, "|||<span").replace(/<\/span>/g,
                            "</span>|||");
                        var g = a.split("|||");
                        g.forEach(function (a) {
                            if ("" !== a || 1 === g.length) {
                                var w = {}, t = I.createElementNS(d.SVG_NS, "tspan"), l, D;
                                (l = v(a, "class")) && C(t, "class", l);
                                if (l = v(a, "style")) l = l.replace(/(;| |^)color([ :])/, "$1fill$2"), C(t, "style", l);
                                (D = v(a, "href")) && !x && (C(t, "onclick", 'location.href="' + D + '"'), C(t, "class", "highcharts-anchor"), d.styledMode || E(t, {cursor: "pointer"}));
                                a = R(a.replace(/<[a-zA-Z\/](.|\n)*?>/g, "") || " ");
                                if (" " !== a) {
                                    t.appendChild(I.createTextNode(a));
                                    c ? w.dx = 0 : b && null !== K && (w.x = K);
                                    C(t, w);
                                    h.appendChild(t);
                                    !c && U && (!O && x && E(t, {display: "block"}), C(t, "dy", f(t)));
                                    if (z) {
                                        var S = a.replace(/([^\^])-/g, "$1- ").split(" ");
                                        w = !J && (1 < g.length || b || 1 < S.length);
                                        D = 0;
                                        var N = f(t);
                                        if (n) r = d.truncate(m, t, a, void 0, 0, Math.max(0, z - parseInt(p || 12, 10)), function (a, b) {
                                            return a.substring(0, b) + "\u2026"
                                        }); else if (w) for (; S.length;) S.length && !J && 0 < D && (t = I.createElementNS(V, "tspan"), C(t, {
                                            dy: N,
                                            x: K
                                        }), l && C(t, "style", l), t.appendChild(I.createTextNode(S.join(" ").replace(/- /g, "-"))), h.appendChild(t)), d.truncate(m, t,
                                            null, S, 0 === D ? e : 0, z, function (a, b) {
                                                return S.slice(0, b).join(" ").replace(/- /g, "-")
                                            }), e = m.actualWidth, D++
                                    }
                                    c++
                                }
                            }
                        });
                        U = U || h.childNodes.length
                    }), n && r && m.attr("title", R(m.textStr, ["&lt;", "&gt;"])), t && t.removeChild(h), D && m.applyTextOutline && m.applyTextOutline(D)) : h.appendChild(I.createTextNode(R(e)))
                }
            }, getContrast: function (a) {
                a = q(a).rgba;
                a[0] *= 1;
                a[1] *= 1.2;
                a[2] *= .5;
                return 459 < a[0] + a[1] + a[2] ? "#000000" : "#FFFFFF"
            }, button: function (a, b, m, c, h, d, e, w, r, O) {
                var t = this.label(a, b, m, r, null, null, O, null, "button"), K = 0, z =
                    this.styledMode;
                t.attr(g({padding: 8, r: 2}, h));
                if (!z) {
                    h = g({
                        fill: "#f7f7f7",
                        stroke: "#cccccc",
                        "stroke-width": 1,
                        style: {color: "#333333", cursor: "pointer", fontWeight: "normal"}
                    }, h);
                    var l = h.style;
                    delete h.style;
                    d = g(h, {fill: "#e6e6e6"}, d);
                    var D = d.style;
                    delete d.style;
                    e = g(h, {fill: "#e6ebf5", style: {color: "#000000", fontWeight: "bold"}}, e);
                    var I = e.style;
                    delete e.style;
                    w = g(h, {style: {color: "#cccccc"}}, w);
                    var n = w.style;
                    delete w.style
                }
                G(t.element, x ? "mouseover" : "mouseenter", function () {
                    3 !== K && t.setState(1)
                });
                G(t.element,
                    x ? "mouseout" : "mouseleave", function () {
                        3 !== K && t.setState(K)
                    });
                t.setState = function (a) {
                    1 !== a && (t.state = K = a);
                    t.removeClass(/highcharts-button-(normal|hover|pressed|disabled)/).addClass("highcharts-button-" + ["normal", "hover", "pressed", "disabled"][a || 0]);
                    z || t.attr([h, d, e, w][a || 0]).css([l, D, I, n][a || 0])
                };
                z || t.attr(h).css(y({cursor: "default"}, l));
                return t.on("click", function (a) {
                    3 !== K && c.call(t, a)
                })
            }, crispLine: function (a, b) {
                a[1] === a[4] && (a[1] = a[4] = Math.round(a[1]) - b % 2 / 2);
                a[2] === a[5] && (a[2] = a[5] = Math.round(a[2]) +
                    b % 2 / 2);
                return a
            }, path: function (a) {
                var b = this.styledMode ? {} : {fill: "none"};
                l(a) ? b.d = a : e(a) && y(b, a);
                return this.createElement("path").attr(b)
            }, circle: function (a, b, m) {
                a = e(a) ? a : "undefined" === typeof a ? {} : {x: a, y: b, r: m};
                b = this.createElement("circle");
                b.xSetter = b.ySetter = function (a, b, m) {
                    m.setAttribute("c" + b, a)
                };
                return b.attr(a)
            }, arc: function (a, b, m, c, h, d) {
                e(a) ? (c = a, b = c.y, m = c.r, a = c.x) : c = {innerR: c, start: h, end: d};
                a = this.symbol("arc", a, b, m, m, c);
                a.r = m;
                return a
            }, rect: function (a, b, m, c, h, d) {
                h = e(a) ? a.r : h;
                var x = this.createElement("rect");
                a = e(a) ? a : "undefined" === typeof a ? {} : {
                    x: a,
                    y: b,
                    width: Math.max(m, 0),
                    height: Math.max(c, 0)
                };
                this.styledMode || ("undefined" !== typeof d && (a.strokeWidth = d, a = x.crisp(a)), a.fill = "none");
                h && (a.r = h);
                x.rSetter = function (a, b, m) {
                    x.r = a;
                    C(m, {rx: a, ry: a})
                };
                x.rGetter = function () {
                    return x.r
                };
                return x.attr(a)
            }, setSize: function (a, m, c) {
                var h = this.alignedObjects, d = h.length;
                this.width = a;
                this.height = m;
                for (this.boxWrapper.animate({width: a, height: m}, {
                    step: function () {
                        this.attr({viewBox: "0 0 " + this.attr("width") + " " + this.attr("height")})
                    },
                    duration: b(c, !0) ? void 0 : 0
                }); d--;) h[d].align()
            }, g: function (a) {
                var b = this.createElement("g");
                return a ? b.attr({"class": "highcharts-" + a}) : b
            }, image: function (a, b, m, c, h, d) {
                var x = {preserveAspectRatio: "none"}, e = function (a, b) {
                    a.setAttributeNS ? a.setAttributeNS("http://www.w3.org/1999/xlink", "href", b) : a.setAttribute("hc-svg-href", b)
                }, g = function (b) {
                    e(w.element, a);
                    d.call(w, b)
                };
                1 < arguments.length && y(x, {x: b, y: m, width: c, height: h});
                var w = this.createElement("image").attr(x);
                d ? (e(w.element, "data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="),
                    x = new X.Image, G(x, "load", g), x.src = a, x.complete && g({})) : e(w.element, a);
                return w
            }, symbol: function (a, m, c, d, x, e) {
                var g = this, w = /^url\((.*?)\)$/, r = w.test(a), O = !r && (this.symbols[a] ? a : "circle"),
                    t = O && this.symbols[O],
                    K = A(m) && t && t.call(this.symbols, Math.round(m), Math.round(c), d, x, e);
                if (t) {
                    var z = this.path(K);
                    g.styledMode || z.attr("fill", "none");
                    y(z, {symbolName: O, x: m, y: c, width: d, height: x});
                    e && y(z, e)
                } else if (r) {
                    var l = a.match(w)[1];
                    z = this.image(l);
                    z.imgwidth = b(U[l] && U[l].width, e && e.width);
                    z.imgheight = b(U[l] && U[l].height,
                        e && e.height);
                    var D = function () {
                        z.attr({width: z.width, height: z.height})
                    };
                    ["width", "height"].forEach(function (a) {
                        z[a + "Setter"] = function (a, b) {
                            var m = {}, c = this["img" + b], h = "width" === b ? "translateX" : "translateY";
                            this[b] = a;
                            A(c) && (e && "within" === e.backgroundSize && this.width && this.height && (c = Math.round(c * Math.min(this.width / this.imgwidth, this.height / this.imgheight))), this.element && this.element.setAttribute(b, c), this.alignByTranslate || (m[h] = ((this[b] || 0) - c) / 2, this.attr(m)))
                        }
                    });
                    A(m) && z.attr({x: m, y: c});
                    z.isImg = !0;
                    A(z.imgwidth) && A(z.imgheight) ? D() : (z.attr({
                        width: 0,
                        height: 0
                    }), F("img", {
                        onload: function () {
                            var a = h[g.chartIndex];
                            0 === this.width && (E(this, {
                                position: "absolute",
                                top: "-999em"
                            }), I.body.appendChild(this));
                            U[l] = {width: this.width, height: this.height};
                            z.imgwidth = this.width;
                            z.imgheight = this.height;
                            z.element && D();
                            this.parentNode && this.parentNode.removeChild(this);
                            g.imgCount--;
                            if (!g.imgCount && a && !a.hasLoaded) a.onload()
                        }, src: l
                    }), this.imgCount++)
                }
                return z
            }, symbols: {
                circle: function (a, b, m, c) {
                    return this.arc(a + m / 2, b +
                        c / 2, m / 2, c / 2, {start: .5 * Math.PI, end: 2.5 * Math.PI, open: !1})
                }, square: function (a, b, m, c) {
                    return ["M", a, b, "L", a + m, b, a + m, b + c, a, b + c, "Z"]
                }, triangle: function (a, b, m, c) {
                    return ["M", a + m / 2, b, "L", a + m, b + c, a, b + c, "Z"]
                }, "triangle-down": function (a, b, m, c) {
                    return ["M", a, b, "L", a + m, b, a + m / 2, b + c, "Z"]
                }, diamond: function (a, b, m, c) {
                    return ["M", a + m / 2, b, "L", a + m, b + c / 2, a + m / 2, b + c, a, b + c / 2, "Z"]
                }, arc: function (a, m, c, h, d) {
                    var x = d.start, e = d.r || c, g = d.r || h || c, w = d.end - .001;
                    c = d.innerR;
                    h = b(d.open, .001 > Math.abs(d.end - d.start - 2 * Math.PI));
                    var r = Math.cos(x),
                        O = Math.sin(x), t = Math.cos(w);
                    w = Math.sin(w);
                    x = b(d.longArc, .001 > d.end - x - Math.PI ? 0 : 1);
                    e = ["M", a + e * r, m + g * O, "A", e, g, 0, x, b(d.clockwise, 1), a + e * t, m + g * w];
                    A(c) && e.push(h ? "M" : "L", a + c * t, m + c * w, "A", c, c, 0, x, A(d.clockwise) ? 1 - d.clockwise : 0, a + c * r, m + c * O);
                    e.push(h ? "" : "Z");
                    return e
                }, callout: function (a, b, m, c, h) {
                    var d = Math.min(h && h.r || 0, m, c), x = d + 6, e = h && h.anchorX;
                    h = h && h.anchorY;
                    var g = ["M", a + d, b, "L", a + m - d, b, "C", a + m, b, a + m, b, a + m, b + d, "L", a + m, b + c - d, "C", a + m, b + c, a + m, b + c, a + m - d, b + c, "L", a + d, b + c, "C", a, b + c, a, b + c, a, b + c - d, "L", a, b + d, "C",
                        a, b, a, b, a + d, b];
                    e && e > m ? h > b + x && h < b + c - x ? g.splice(13, 3, "L", a + m, h - 6, a + m + 6, h, a + m, h + 6, a + m, b + c - d) : g.splice(13, 3, "L", a + m, c / 2, e, h, a + m, c / 2, a + m, b + c - d) : e && 0 > e ? h > b + x && h < b + c - x ? g.splice(33, 3, "L", a, h + 6, a - 6, h, a, h - 6, a, b + d) : g.splice(33, 3, "L", a, c / 2, e, h, a, c / 2, a, b + d) : h && h > c && e > a + x && e < a + m - x ? g.splice(23, 3, "L", e + 6, b + c, e, b + c + 6, e - 6, b + c, a + d, b + c) : h && 0 > h && e > a + x && e < a + m - x && g.splice(3, 3, "L", e - 6, b, e, b - 6, e + 6, b, m - d, b);
                    return g
                }
            }, clipRect: function (a, b, m, c) {
                var h = r() + "-", d = this.createElement("clipPath").attr({id: h}).add(this.defs);
                a = this.rect(a,
                    b, m, c, 0).add(d);
                a.id = h;
                a.clipPath = d;
                a.count = 0;
                return a
            }, text: function (a, b, m, c) {
                var h = {};
                if (c && (this.allowHTML || !this.forExport)) return this.html(a, b, m);
                h.x = Math.round(b || 0);
                m && (h.y = Math.round(m));
                A(a) && (h.text = a);
                a = this.createElement("text").attr(h);
                c || (a.xSetter = function (a, b, m) {
                    var c = m.getElementsByTagName("tspan"), h = m.getAttribute(b), d;
                    for (d = 0; d < c.length; d++) {
                        var x = c[d];
                        x.getAttribute(b) === h && x.setAttribute(b, a)
                    }
                    m.setAttribute(b, a)
                });
                return a
            }, fontMetrics: function (b, m) {
                b = !this.styledMode && /px/.test(b) ||
                !X.getComputedStyle ? b || m && m.style && m.style.fontSize || this.style && this.style.fontSize : m && Q.prototype.getStyle.call(m, "font-size");
                b = /px/.test(b) ? a(b) : 12;
                m = 24 > b ? b + 3 : Math.round(1.2 * b);
                return {h: m, b: Math.round(.8 * m), f: b}
            }, rotCorr: function (a, b, m) {
                var c = a;
                b && m && (c = Math.max(c * Math.cos(b * t), 4));
                return {x: -a / 3 * Math.sin(b * t), y: c}
            }, label: function (a, b, m, c, h, d, x, e, r) {
                var O = this, t = O.styledMode, K = O.g("button" !== r && "label"),
                    z = K.text = O.text("", 0, 0, x).attr({zIndex: 1}), l, D, I = 0, J = 3, V = 0, p, U, N, f, v,
                    X = {}, S, k, B = /^url\((.*?)\)$/.test(c),
                    ba = t || B, E = function () {
                        return t ? l.strokeWidth() % 2 / 2 : (S ? parseInt(S, 10) : 0) % 2 / 2
                    };
                r && K.addClass("highcharts-" + r);
                var q = function () {
                    var a = z.element.style, b = {};
                    D = ("undefined" === typeof p || "undefined" === typeof U || v) && A(z.textStr) && z.getBBox();
                    K.width = (p || D.width || 0) + 2 * J + V;
                    K.height = (U || D.height || 0) + 2 * J;
                    k = J + Math.min(O.fontMetrics(a && a.fontSize, z).b, D ? D.height : Infinity);
                    ba && (l || (K.box = l = O.symbols[c] || B ? O.symbol(c) : O.rect(), l.addClass(("button" === r ? "" : "highcharts-label-box") + (r ? " highcharts-" + r + "-box" : "")), l.add(K),
                        a = E(), b.x = a, b.y = (e ? -k : 0) + a), b.width = Math.round(K.width), b.height = Math.round(K.height), l.attr(y(b, X)), X = {})
                };
                var R = function () {
                    var a = V + J;
                    var b = e ? 0 : k;
                    A(p) && D && ("center" === v || "right" === v) && (a += {center: .5, right: 1}[v] * (p - D.width));
                    if (a !== z.x || b !== z.y) z.attr("x", a), z.hasBoxWidthChanged && (D = z.getBBox(!0), q()), "undefined" !== typeof b && z.attr("y", b);
                    z.x = a;
                    z.y = b
                };
                var T = function (a, b) {
                    l ? l.attr(a, b) : X[a] = b
                };
                K.onAdd = function () {
                    z.add(K);
                    K.attr({text: a || 0 === a ? a : "", x: b, y: m});
                    l && A(h) && K.attr({anchorX: h, anchorY: d})
                };
                K.widthSetter = function (a) {
                    p = n(a) ? a : null
                };
                K.heightSetter = function (a) {
                    U = a
                };
                K["text-alignSetter"] = function (a) {
                    v = a
                };
                K.paddingSetter = function (a) {
                    A(a) && a !== J && (J = K.padding = a, R())
                };
                K.paddingLeftSetter = function (a) {
                    A(a) && a !== V && (V = a, R())
                };
                K.alignSetter = function (a) {
                    a = {left: 0, center: .5, right: 1}[a];
                    a !== I && (I = a, D && K.attr({x: N}))
                };
                K.textSetter = function (a) {
                    "undefined" !== typeof a && z.attr({text: a});
                    q();
                    R()
                };
                K["stroke-widthSetter"] = function (a, b) {
                    a && (ba = !0);
                    S = this["stroke-width"] = a;
                    T(b, a)
                };
                t ? K.rSetter = function (a, b) {
                    T(b,
                        a)
                } : K.strokeSetter = K.fillSetter = K.rSetter = function (a, b) {
                    "r" !== b && ("fill" === b && a && (ba = !0), K[b] = a);
                    T(b, a)
                };
                K.anchorXSetter = function (a, b) {
                    h = K.anchorX = a;
                    T(b, Math.round(a) - E() - N)
                };
                K.anchorYSetter = function (a, b) {
                    d = K.anchorY = a;
                    T(b, a - f)
                };
                K.xSetter = function (a) {
                    K.x = a;
                    I && (a -= I * ((p || D.width) + 2 * J), K["forceAnimate:x"] = !0);
                    N = Math.round(a);
                    K.attr("translateX", N)
                };
                K.ySetter = function (a) {
                    f = K.y = Math.round(a);
                    K.attr("translateY", f)
                };
                var C = K.css;
                x = {
                    css: function (a) {
                        if (a) {
                            var b = {};
                            a = g(a);
                            K.textProps.forEach(function (m) {
                                "undefined" !==
                                typeof a[m] && (b[m] = a[m], delete a[m])
                            });
                            z.css(b);
                            "width" in b && q();
                            "fontSize" in b && (q(), R())
                        }
                        return C.call(K, a)
                    }, getBBox: function () {
                        return {width: D.width + 2 * J, height: D.height + 2 * J, x: D.x - J, y: D.y - J}
                    }, destroy: function () {
                        w(K.element, "mouseenter");
                        w(K.element, "mouseleave");
                        z && (z = z.destroy());
                        l && (l = l.destroy());
                        Q.prototype.destroy.call(K);
                        K = O = q = R = T = null
                    }
                };
                t || (x.shadow = function (a) {
                    a && (q(), l && l.shadow(a));
                    return K
                });
                return y(K, x)
            }
        });
        f.Renderer = k
    });
    P(u, "parts/Html.js", [u["parts/Globals.js"], u["parts/Utilities.js"]],
        function (f, k) {
            var H = k.attr, q = k.createElement, G = k.css, M = k.defined, L = k.extend, C = k.pick, F = k.pInt,
                E = f.isFirefox, A = f.isMS, v = f.isWebKit, B = f.SVGElement;
            k = f.SVGRenderer;
            var y = f.win;
            L(B.prototype, {
                htmlCss: function (p) {
                    var l = "SPAN" === this.element.tagName && p && "width" in p, n = C(l && p.width, void 0);
                    if (l) {
                        delete p.width;
                        this.textWidth = n;
                        var e = !0
                    }
                    p && "ellipsis" === p.textOverflow && (p.whiteSpace = "nowrap", p.overflow = "hidden");
                    this.styles = L(this.styles, p);
                    G(this.element, p);
                    e && this.htmlUpdateTransform();
                    return this
                }, htmlGetBBox: function () {
                    var p =
                        this.element;
                    return {x: p.offsetLeft, y: p.offsetTop, width: p.offsetWidth, height: p.offsetHeight}
                }, htmlUpdateTransform: function () {
                    if (this.added) {
                        var p = this.renderer, l = this.element, n = this.translateX || 0, e = this.translateY || 0,
                            d = this.x || 0, g = this.y || 0, c = this.textAlign || "left",
                            b = {left: 0, center: .5, right: 1}[c], a = this.styles, w = a && a.whiteSpace;
                        G(l, {marginLeft: n, marginTop: e});
                        !p.styledMode && this.shadows && this.shadows.forEach(function (a) {
                            G(a, {marginLeft: n + 1, marginTop: e + 1})
                        });
                        this.inverted && [].forEach.call(l.childNodes,
                            function (a) {
                                p.invertChild(a, l)
                            });
                        if ("SPAN" === l.tagName) {
                            a = this.rotation;
                            var z = this.textWidth && F(this.textWidth),
                                D = [a, c, l.innerHTML, this.textWidth, this.textAlign].join(), r;
                            (r = z !== this.oldTextWidth) && !(r = z > this.oldTextWidth) && ((r = this.textPxLength) || (G(l, {
                                width: "",
                                whiteSpace: w || "nowrap"
                            }), r = l.offsetWidth), r = r > z);
                            r && (/[ \-]/.test(l.textContent || l.innerText) || "ellipsis" === l.style.textOverflow) ? (G(l, {
                                    width: z + "px",
                                    display: "block",
                                    whiteSpace: w || "normal"
                                }), this.oldTextWidth = z, this.hasBoxWidthChanged = !0) :
                                this.hasBoxWidthChanged = !1;
                            D !== this.cTT && (w = p.fontMetrics(l.style.fontSize, l).b, !M(a) || a === (this.oldRotation || 0) && c === this.oldAlign || this.setSpanRotation(a, b, w), this.getSpanCorrection(!M(a) && this.textPxLength || l.offsetWidth, w, b, a, c));
                            G(l, {left: d + (this.xCorr || 0) + "px", top: g + (this.yCorr || 0) + "px"});
                            this.cTT = D;
                            this.oldRotation = a;
                            this.oldAlign = c
                        }
                    } else this.alignOnAdd = !0
                }, setSpanRotation: function (p, l, n) {
                    var e = {}, d = this.renderer.getTransformKey();
                    e[d] = e.transform = "rotate(" + p + "deg)";
                    e[d + (E ? "Origin" : "-origin")] =
                        e.transformOrigin = 100 * l + "% " + n + "px";
                    G(this.element, e)
                }, getSpanCorrection: function (p, l, n) {
                    this.xCorr = -p * n;
                    this.yCorr = -l
                }
            });
            L(k.prototype, {
                getTransformKey: function () {
                    return A && !/Edge/.test(y.navigator.userAgent) ? "-ms-transform" : v ? "-webkit-transform" : E ? "MozTransform" : y.opera ? "-o-transform" : ""
                }, html: function (p, l, n) {
                    var e = this.createElement("span"), d = e.element, g = e.renderer, c = g.isSVG,
                        b = function (a, b) {
                            ["opacity", "visibility"].forEach(function (c) {
                                a[c + "Setter"] = function (d, e, h) {
                                    var g = a.div ? a.div.style : b;
                                    B.prototype[c +
                                    "Setter"].call(this, d, e, h);
                                    g && (g[e] = d)
                                }
                            });
                            a.addedSetters = !0
                        };
                    e.textSetter = function (a) {
                        a !== d.innerHTML && (delete this.bBox, delete this.oldTextWidth);
                        this.textStr = a;
                        d.innerHTML = C(a, "");
                        e.doTransform = !0
                    };
                    c && b(e, e.element.style);
                    e.xSetter = e.ySetter = e.alignSetter = e.rotationSetter = function (a, b) {
                        "align" === b && (b = "textAlign");
                        e[b] = a;
                        e.doTransform = !0
                    };
                    e.afterSetters = function () {
                        this.doTransform && (this.htmlUpdateTransform(), this.doTransform = !1)
                    };
                    e.attr({text: p, x: Math.round(l), y: Math.round(n)}).css({position: "absolute"});
                    g.styledMode || e.css({fontFamily: this.style.fontFamily, fontSize: this.style.fontSize});
                    d.style.whiteSpace = "nowrap";
                    e.css = e.htmlCss;
                    c && (e.add = function (a) {
                        var c = g.box.parentNode, z = [];
                        if (this.parentGroup = a) {
                            var l = a.div;
                            if (!l) {
                                for (; a;) z.push(a), a = a.parentGroup;
                                z.reverse().forEach(function (a) {
                                    function h(b, c) {
                                        a[c] = b;
                                        "translateX" === c ? g.left = b + "px" : g.top = b + "px";
                                        a.doTransform = !0
                                    }

                                    var d = H(a.element, "class");
                                    l = a.div = a.div || q("div", d ? {className: d} : void 0, {
                                        position: "absolute",
                                        left: (a.translateX || 0) + "px",
                                        top: (a.translateY ||
                                            0) + "px",
                                        display: a.display,
                                        opacity: a.opacity,
                                        pointerEvents: a.styles && a.styles.pointerEvents
                                    }, l || c);
                                    var g = l.style;
                                    L(a, {
                                        classSetter: function (a) {
                                            return function (b) {
                                                this.element.setAttribute("class", b);
                                                a.className = b
                                            }
                                        }(l), on: function () {
                                            z[0].div && e.on.apply({element: z[0].div}, arguments);
                                            return a
                                        }, translateXSetter: h, translateYSetter: h
                                    });
                                    a.addedSetters || b(a)
                                })
                            }
                        } else l = c;
                        l.appendChild(d);
                        e.added = !0;
                        e.alignOnAdd && e.htmlUpdateTransform();
                        return e
                    });
                    return e
                }
            })
        });
    P(u, "parts/Tick.js", [u["parts/Globals.js"], u["parts/Utilities.js"]],
        function (f, k) {
            var H = k.clamp, q = k.correctFloat, G = k.defined, M = k.destroyObjectProperties, L = k.extend,
                C = k.isNumber, F = k.merge, E = k.objectEach, A = k.pick, v = f.fireEvent, B = f.deg2rad;
            k = function () {
                function y(p, l, n, e, d) {
                    this.isNewLabel = this.isNew = !0;
                    this.axis = p;
                    this.pos = l;
                    this.type = n || "";
                    this.parameters = d || {};
                    this.tickmarkOffset = this.parameters.tickmarkOffset;
                    this.options = this.parameters.options;
                    n || e || this.addLabel()
                }

                y.prototype.addLabel = function () {
                    var p = this, l = p.axis, n = l.options, e = l.chart, d = l.categories, g = l.names,
                        c = p.pos, b = A(p.options && p.options.labels, n.labels), a = l.tickPositions, w = c === a[0],
                        z = c === a[a.length - 1];
                    g = this.parameters.category || (d ? A(d[c], g[c], c) : c);
                    var D = p.label;
                    d = (!b.step || 1 === b.step) && 1 === l.tickInterval;
                    a = a.info;
                    var r, h;
                    if (l.isDatetimeAxis && a) {
                        var t = e.time.resolveDTLFormat(n.dateTimeLabelFormats[!n.grid && a.higherRanks[c] || a.unitName]);
                        var I = t.main
                    }
                    p.isFirst = w;
                    p.isLast = z;
                    p.formatCtx = {
                        axis: l,
                        chart: e,
                        isFirst: w,
                        isLast: z,
                        dateTimeLabelFormat: I,
                        tickPositionInfo: a,
                        value: l.isLog ? q(l.lin2log(g)) : g,
                        pos: c
                    };
                    n = l.labelFormatter.call(p.formatCtx, this.formatCtx);
                    if (h = t && t.list) p.shortenLabel = function () {
                        for (r = 0; r < h.length; r++) if (D.attr({text: l.labelFormatter.call(L(p.formatCtx, {dateTimeLabelFormat: h[r]}))}), D.getBBox().width < l.getSlotWidth(p) - 2 * A(b.padding, 5)) return;
                        D.attr({text: ""})
                    };
                    d && l._addedPlotLB && l.isXAxis && p.moveLabel(n, b);
                    G(D) || p.movedLabel ? D && D.textStr !== n && !d && (!D.textWidth || b.style && b.style.width || D.styles.width || D.css({width: null}), D.attr({text: n}), D.textPxLength = D.getBBox().width) : (p.label =
                        D = p.createLabel({x: 0, y: 0}, n, b), p.rotation = 0)
                };
                y.prototype.createLabel = function (p, l, n) {
                    var e = this.axis, d = e.chart;
                    if (p = G(l) && n.enabled ? d.renderer.text(l, p.x, p.y, n.useHTML).add(e.labelGroup) : null) d.styledMode || p.css(F(n.style)), p.textPxLength = p.getBBox().width;
                    return p
                };
                y.prototype.destroy = function () {
                    M(this, this.axis)
                };
                y.prototype.getPosition = function (p, l, n, e) {
                    var d = this.axis, g = d.chart, c = e && g.oldChartHeight || g.chartHeight;
                    p = {
                        x: p ? q(d.translate(l + n, null, null, e) + d.transB) : d.left + d.offset + (d.opposite ? (e &&
                            g.oldChartWidth || g.chartWidth) - d.right - d.left : 0),
                        y: p ? c - d.bottom + d.offset - (d.opposite ? d.height : 0) : q(c - d.translate(l + n, null, null, e) - d.transB)
                    };
                    p.y = H(p.y, -1E5, 1E5);
                    v(this, "afterGetPosition", {pos: p});
                    return p
                };
                y.prototype.getLabelPosition = function (p, l, n, e, d, g, c, b) {
                    var a = this.axis, w = a.transA,
                        z = a.isLinked && a.linkedParent ? a.linkedParent.reversed : a.reversed, D = a.staggerLines,
                        r = a.tickRotCorr || {x: 0, y: 0}, h = d.y,
                        t = e || a.reserveSpaceDefault ? 0 : -a.labelOffset * ("center" === a.labelAlign ? .5 : 1),
                        I = {};
                    G(h) || (h = 0 === a.side ?
                        n.rotation ? -8 : -n.getBBox().height : 2 === a.side ? r.y + 8 : Math.cos(n.rotation * B) * (r.y - n.getBBox(!1, 0).height / 2));
                    p = p + d.x + t + r.x - (g && e ? g * w * (z ? -1 : 1) : 0);
                    l = l + h - (g && !e ? g * w * (z ? 1 : -1) : 0);
                    D && (n = c / (b || 1) % D, a.opposite && (n = D - n - 1), l += a.labelOffset / D * n);
                    I.x = p;
                    I.y = Math.round(l);
                    v(this, "afterGetLabelPosition", {pos: I, tickmarkOffset: g, index: c});
                    return I
                };
                y.prototype.getLabelSize = function () {
                    return this.label ? this.label.getBBox()[this.axis.horiz ? "height" : "width"] : 0
                };
                y.prototype.getMarkPath = function (p, l, n, e, d, g) {
                    return g.crispLine(["M",
                        p, l, "L", p + (d ? 0 : -n), l + (d ? n : 0)], e)
                };
                y.prototype.handleOverflow = function (p) {
                    var l = this.axis, n = l.options.labels, e = p.x, d = l.chart.chartWidth, g = l.chart.spacing,
                        c = A(l.labelLeft, Math.min(l.pos, g[3]));
                    g = A(l.labelRight, Math.max(l.isRadial ? 0 : l.pos + l.len, d - g[1]));
                    var b = this.label, a = this.rotation,
                        w = {left: 0, center: .5, right: 1}[l.labelAlign || b.attr("align")], z = b.getBBox().width,
                        D = l.getSlotWidth(this), r = D, h = 1, t, I = {};
                    if (a || "justify" !== A(n.overflow, "justify")) 0 > a && e - w * z < c ? t = Math.round(e / Math.cos(a * B) - c) : 0 < a && e + w * z > g &&
                        (t = Math.round((d - e) / Math.cos(a * B))); else if (d = e + (1 - w) * z, e - w * z < c ? r = p.x + r * (1 - w) - c : d > g && (r = g - p.x + r * w, h = -1), r = Math.min(D, r), r < D && "center" === l.labelAlign && (p.x += h * (D - r - w * (D - Math.min(z, r)))), z > r || l.autoRotation && (b.styles || {}).width) t = r;
                    t && (this.shortenLabel ? this.shortenLabel() : (I.width = Math.floor(t), (n.style || {}).textOverflow || (I.textOverflow = "ellipsis"), b.css(I)))
                };
                y.prototype.moveLabel = function (p, l) {
                    var n = this, e = n.label, d = !1, g = n.axis, c = g.reversed, b = g.chart.inverted;
                    e && e.textStr === p ? (n.movedLabel = e, d =
                        !0, delete n.label) : E(g.ticks, function (a) {
                        d || a.isNew || a === n || !a.label || a.label.textStr !== p || (n.movedLabel = a.label, d = !0, a.labelPos = n.movedLabel.xy, delete a.label)
                    });
                    if (!d && (n.labelPos || e)) {
                        var a = n.labelPos || e.xy;
                        e = b ? a.x : c ? 0 : g.width + g.left;
                        g = b ? c ? g.width + g.left : 0 : a.y;
                        n.movedLabel = n.createLabel({x: e, y: g}, p, l);
                        n.movedLabel && n.movedLabel.attr({opacity: 0})
                    }
                };
                y.prototype.render = function (p, l, n) {
                    var e = this.axis, d = e.horiz, g = this.pos, c = A(this.tickmarkOffset, e.tickmarkOffset);
                    g = this.getPosition(d, g, c, l);
                    c = g.x;
                    var b = g.y;
                    e = d && c === e.pos + e.len || !d && b === e.pos ? -1 : 1;
                    n = A(n, 1);
                    this.isActive = !0;
                    this.renderGridLine(l, n, e);
                    this.renderMark(g, n, e);
                    this.renderLabel(g, l, n, p);
                    this.isNew = !1;
                    f.fireEvent(this, "afterRender")
                };
                y.prototype.renderGridLine = function (p, l, n) {
                    var e = this.axis, d = e.options, g = this.gridLine, c = {}, b = this.pos, a = this.type,
                        w = A(this.tickmarkOffset, e.tickmarkOffset), z = e.chart.renderer, D = a ? a + "Grid" : "grid",
                        r = d[D + "LineWidth"], h = d[D + "LineColor"];
                    d = d[D + "LineDashStyle"];
                    g || (e.chart.styledMode || (c.stroke = h, c["stroke-width"] =
                        r, d && (c.dashstyle = d)), a || (c.zIndex = 1), p && (l = 0), this.gridLine = g = z.path().attr(c).addClass("highcharts-" + (a ? a + "-" : "") + "grid-line").add(e.gridGroup));
                    if (g && (n = e.getPlotLinePath({
                        value: b + w,
                        lineWidth: g.strokeWidth() * n,
                        force: "pass",
                        old: p
                    }))) g[p || this.isNew ? "attr" : "animate"]({d: n, opacity: l})
                };
                y.prototype.renderMark = function (p, l, n) {
                    var e = this.axis, d = e.options, g = e.chart.renderer, c = this.type, b = c ? c + "Tick" : "tick",
                        a = e.tickSize(b), w = this.mark, z = !w, D = p.x;
                    p = p.y;
                    var r = A(d[b + "Width"], !c && e.isXAxis ? 1 : 0);
                    d = d[b + "Color"];
                    a && (e.opposite && (a[0] = -a[0]), z && (this.mark = w = g.path().addClass("highcharts-" + (c ? c + "-" : "") + "tick").add(e.axisGroup), e.chart.styledMode || w.attr({
                        stroke: d,
                        "stroke-width": r
                    })), w[z ? "attr" : "animate"]({
                        d: this.getMarkPath(D, p, a[0], w.strokeWidth() * n, e.horiz, g),
                        opacity: l
                    }))
                };
                y.prototype.renderLabel = function (p, l, n, e) {
                    var d = this.axis, g = d.horiz, c = d.options, b = this.label, a = c.labels, w = a.step;
                    d = A(this.tickmarkOffset, d.tickmarkOffset);
                    var z = !0, D = p.x;
                    p = p.y;
                    b && C(D) && (b.xy = p = this.getLabelPosition(D, p, b, g, a, d, e, w), this.isFirst &&
                    !this.isLast && !A(c.showFirstLabel, 1) || this.isLast && !this.isFirst && !A(c.showLastLabel, 1) ? z = !1 : !g || a.step || a.rotation || l || 0 === n || this.handleOverflow(p), w && e % w && (z = !1), z && C(p.y) ? (p.opacity = n, b[this.isNewLabel ? "attr" : "animate"](p), this.isNewLabel = !1) : (b.attr("y", -9999), this.isNewLabel = !0))
                };
                y.prototype.replaceMovedLabel = function () {
                    var p = this.label, l = this.axis, n = l.reversed, e = this.axis.chart.inverted;
                    if (p && !this.isNew) {
                        var d = e ? p.xy.x : n ? l.left : l.width + l.left;
                        n = e ? n ? l.width + l.top : l.top : p.xy.y;
                        p.animate({
                            x: d,
                            y: n, opacity: 0
                        }, void 0, p.destroy);
                        delete this.label
                    }
                    l.isDirty = !0;
                    this.label = this.movedLabel;
                    delete this.movedLabel
                };
                return y
            }();
            f.Tick = k;
            return f.Tick
        });
    P(u, "parts/Time.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.defined, q = k.error, G = k.extend, M = k.isObject, L = k.merge, C = k.objectEach, F = k.pad,
            E = k.pick, A = k.splat, v = k.timeUnits, B = f.win;
        k = function () {
            function y(p) {
                this.options = {};
                this.variableTimezone = this.useUTC = !1;
                this.Date = B.Date;
                this.getTimezoneOffset = this.timezoneOffsetFunction();
                this.update(p)
            }

            y.prototype.get = function (p, l) {
                if (this.variableTimezone || this.timezoneOffset) {
                    var n = l.getTime(), e = n - this.getTimezoneOffset(l);
                    l.setTime(e);
                    p = l["getUTC" + p]();
                    l.setTime(n);
                    return p
                }
                return this.useUTC ? l["getUTC" + p]() : l["get" + p]()
            };
            y.prototype.set = function (p, l, n) {
                if (this.variableTimezone || this.timezoneOffset) {
                    if ("Milliseconds" === p || "Seconds" === p || "Minutes" === p) return l["setUTC" + p](n);
                    var e = this.getTimezoneOffset(l);
                    e = l.getTime() - e;
                    l.setTime(e);
                    l["setUTC" + p](n);
                    p = this.getTimezoneOffset(l);
                    e = l.getTime() + p;
                    return l.setTime(e)
                }
                return this.useUTC ? l["setUTC" + p](n) : l["set" + p](n)
            };
            y.prototype.update = function (p) {
                var l = E(p && p.useUTC, !0);
                this.options = p = L(!0, this.options || {}, p);
                this.Date = p.Date || B.Date || Date;
                this.timezoneOffset = (this.useUTC = l) && p.timezoneOffset;
                this.getTimezoneOffset = this.timezoneOffsetFunction();
                this.variableTimezone = !(l && !p.getTimezoneOffset && !p.timezone)
            };
            y.prototype.makeTime = function (p, l, n, e, d, g) {
                if (this.useUTC) {
                    var c = this.Date.UTC.apply(0, arguments);
                    var b = this.getTimezoneOffset(c);
                    c += b;
                    var a = this.getTimezoneOffset(c);
                    b !== a ? c += a - b : b - 36E5 !== this.getTimezoneOffset(c - 36E5) || f.isSafari || (c -= 36E5)
                } else c = (new this.Date(p, l, E(n, 1), E(e, 0), E(d, 0), E(g, 0))).getTime();
                return c
            };
            y.prototype.timezoneOffsetFunction = function () {
                var p = this, l = this.options, n = B.moment;
                if (!this.useUTC) return function (e) {
                    return 6E4 * (new Date(e.toString())).getTimezoneOffset()
                };
                if (l.timezone) {
                    if (n) return function (e) {
                        return 6E4 * -n.tz(e, l.timezone).utcOffset()
                    };
                    q(25)
                }
                return this.useUTC && l.getTimezoneOffset ? function (e) {
                    return 6E4 *
                        l.getTimezoneOffset(e.valueOf())
                } : function () {
                    return 6E4 * (p.timezoneOffset || 0)
                }
            };
            y.prototype.dateFormat = function (p, l, n) {
                var e;
                if (!H(l) || isNaN(l)) return (null === (e = f.defaultOptions.lang) || void 0 === e ? void 0 : e.invalidDate) || "";
                p = E(p, "%Y-%m-%d %H:%M:%S");
                var d = this;
                e = new this.Date(l);
                var g = this.get("Hours", e), c = this.get("Day", e), b = this.get("Date", e), a = this.get("Month", e),
                    w = this.get("FullYear", e), z = f.defaultOptions.lang,
                    D = null === z || void 0 === z ? void 0 : z.weekdays,
                    r = null === z || void 0 === z ? void 0 : z.shortWeekdays;
                e = G({
                    a: r ? r[c] : D[c].substr(0, 3),
                    A: D[c],
                    d: F(b),
                    e: F(b, 2, " "),
                    w: c,
                    b: z.shortMonths[a],
                    B: z.months[a],
                    m: F(a + 1),
                    o: a + 1,
                    y: w.toString().substr(2, 2),
                    Y: w,
                    H: F(g),
                    k: g,
                    I: F(g % 12 || 12),
                    l: g % 12 || 12,
                    M: F(this.get("Minutes", e)),
                    p: 12 > g ? "AM" : "PM",
                    P: 12 > g ? "am" : "pm",
                    S: F(e.getSeconds()),
                    L: F(Math.floor(l % 1E3), 3)
                }, f.dateFormats);
                C(e, function (a, b) {
                    for (; -1 !== p.indexOf("%" + b);) p = p.replace("%" + b, "function" === typeof a ? a.call(d, l) : a)
                });
                return n ? p.substr(0, 1).toUpperCase() + p.substr(1) : p
            };
            y.prototype.resolveDTLFormat = function (p) {
                return M(p,
                    !0) ? p : (p = A(p), {main: p[0], from: p[1], to: p[2]})
            };
            y.prototype.getTimeTicks = function (p, l, n, e) {
                var d = this, g = [], c = {};
                var b = new d.Date(l);
                var a = p.unitRange, w = p.count || 1, z;
                e = E(e, 1);
                if (H(l)) {
                    d.set("Milliseconds", b, a >= v.second ? 0 : w * Math.floor(d.get("Milliseconds", b) / w));
                    a >= v.second && d.set("Seconds", b, a >= v.minute ? 0 : w * Math.floor(d.get("Seconds", b) / w));
                    a >= v.minute && d.set("Minutes", b, a >= v.hour ? 0 : w * Math.floor(d.get("Minutes", b) / w));
                    a >= v.hour && d.set("Hours", b, a >= v.day ? 0 : w * Math.floor(d.get("Hours", b) / w));
                    a >= v.day &&
                    d.set("Date", b, a >= v.month ? 1 : Math.max(1, w * Math.floor(d.get("Date", b) / w)));
                    if (a >= v.month) {
                        d.set("Month", b, a >= v.year ? 0 : w * Math.floor(d.get("Month", b) / w));
                        var D = d.get("FullYear", b)
                    }
                    a >= v.year && d.set("FullYear", b, D - D % w);
                    a === v.week && (D = d.get("Day", b), d.set("Date", b, d.get("Date", b) - D + e + (D < e ? -7 : 0)));
                    D = d.get("FullYear", b);
                    e = d.get("Month", b);
                    var r = d.get("Date", b), h = d.get("Hours", b);
                    l = b.getTime();
                    d.variableTimezone && (z = n - l > 4 * v.month || d.getTimezoneOffset(l) !== d.getTimezoneOffset(n));
                    l = b.getTime();
                    for (b = 1; l < n;) g.push(l),
                        l = a === v.year ? d.makeTime(D + b * w, 0) : a === v.month ? d.makeTime(D, e + b * w) : !z || a !== v.day && a !== v.week ? z && a === v.hour && 1 < w ? d.makeTime(D, e, r, h + b * w) : l + a * w : d.makeTime(D, e, r + b * w * (a === v.day ? 1 : 7)), b++;
                    g.push(l);
                    a <= v.hour && 1E4 > g.length && g.forEach(function (a) {
                        0 === a % 18E5 && "000000000" === d.dateFormat("%H%M%S%L", a) && (c[a] = "day")
                    })
                }
                g.info = G(p, {higherRanks: c, totalRange: a * w});
                return g
            };
            y.defaultOptions = {
                Date: void 0,
                getTimezoneOffset: void 0,
                timezone: void 0,
                timezoneOffset: 0,
                useUTC: !0
            };
            return y
        }();
        f.Time = k;
        return f.Time
    });
    P(u,
        "parts/Options.js", [u["parts/Globals.js"], u["parts/Time.js"], u["parts/Color.js"], u["parts/Utilities.js"]], function (f, k, H, q) {
            H = H.parse;
            var G = q.merge;
            f.defaultOptions = {
                colors: "#7cb5ec #434348 #90ed7d #f7a35c #8085e9 #f15c80 #e4d354 #2b908f #f45b5b #91e8e1".split(" "),
                symbols: ["circle", "diamond", "square", "triangle", "triangle-down"],
                lang: {
                    loading: "Loading...",
                    months: "January February March April May June July August September October November December".split(" "),
                    shortMonths: "Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec".split(" "),
                    weekdays: "Sunday Monday Tuesday Wednesday Thursday Friday Saturday".split(" "),
                    decimalPoint: ".",
                    numericSymbols: "kMGTPE".split(""),
                    resetZoom: "Reset zoom",
                    resetZoomTitle: "Reset zoom level 1:1",
                    thousandsSep: " "
                },
                global: {},
                time: k.defaultOptions,
                chart: {
                    styledMode: !1,
                    borderRadius: 0,
                    colorCount: 10,
                    defaultSeriesType: "line",
                    ignoreHiddenSeries: !0,
                    spacing: [10, 10, 15, 10],
                    resetZoomButton: {theme: {zIndex: 6}, position: {align: "right", x: -10, y: 10}},
                    width: null,
                    height: null,
                    borderColor: "#335cad",
                    backgroundColor: "#ffffff",
                    plotBorderColor: "#cccccc"
                },
                title: {text: "Chart title", align: "center", margin: 15, widthAdjust: -44},
                subtitle: {text: "", align: "center", widthAdjust: -44},
                caption: {margin: 15, text: "", align: "left", verticalAlign: "bottom"},
                plotOptions: {},
                labels: {style: {position: "absolute", color: "#333333"}},
                legend: {
                    enabled: !0,
                    align: "center",
                    alignColumns: !0,
                    layout: "horizontal",
                    labelFormatter: function () {
                        return this.name
                    },
                    borderColor: "#999999",
                    borderRadius: 0,
                    navigation: {activeColor: "#003399", inactiveColor: "#cccccc"},
                    itemStyle: {
                        color: "#333333",
                        cursor: "pointer", fontSize: "12px", fontWeight: "bold", textOverflow: "ellipsis"
                    },
                    itemHoverStyle: {color: "#000000"},
                    itemHiddenStyle: {color: "#cccccc"},
                    shadow: !1,
                    itemCheckboxStyle: {position: "absolute", width: "13px", height: "13px"},
                    squareSymbol: !0,
                    symbolPadding: 5,
                    verticalAlign: "bottom",
                    x: 0,
                    y: 0,
                    title: {style: {fontWeight: "bold"}}
                },
                loading: {
                    labelStyle: {fontWeight: "bold", position: "relative", top: "45%"},
                    style: {position: "absolute", backgroundColor: "#ffffff", opacity: .5, textAlign: "center"}
                },
                tooltip: {
                    enabled: !0,
                    animation: f.svg,
                    borderRadius: 3,
                    dateTimeLabelFormats: {
                        millisecond: "%A, %b %e, %H:%M:%S.%L",
                        second: "%A, %b %e, %H:%M:%S",
                        minute: "%A, %b %e, %H:%M",
                        hour: "%A, %b %e, %H:%M",
                        day: "%A, %b %e, %Y",
                        week: "Week from %A, %b %e, %Y",
                        month: "%B %Y",
                        year: "%Y"
                    },
                    footerFormat: "",
                    padding: 8,
                    snap: f.isTouchDevice ? 25 : 10,
                    headerFormat: '<span style="font-size: 10px">{point.key}</span><br/>',
                    pointFormat: '<span style="color:{point.color}">\u25cf</span> {series.name}: <b>{point.y}</b><br/>',
                    backgroundColor: H("#f7f7f7").setOpacity(.85).get(),
                    borderWidth: 1,
                    shadow: !0,
                    style: {color: "#333333", cursor: "default", fontSize: "12px", whiteSpace: "nowrap"}
                },
                credits: {
                    enabled: !0,
                    href: "https://www.highcharts.com?credits",
                    position: {align: "right", x: -10, verticalAlign: "bottom", y: -5},
                    style: {cursor: "pointer", color: "#999999", fontSize: "9px"},
                    text: "Highcharts.com"
                }
            };
            f.setOptions = function (k) {
                f.defaultOptions = G(!0, f.defaultOptions, k);
                (k.time || k.global) && f.time.update(G(f.defaultOptions.global, f.defaultOptions.time, k.global, k.time));
                return f.defaultOptions
            };
            f.getOptions =
                function () {
                    return f.defaultOptions
                };
            f.defaultPlotOptions = f.defaultOptions.plotOptions;
            f.time = new k(G(f.defaultOptions.global, f.defaultOptions.time));
            f.dateFormat = function (k, q, C) {
                return f.time.dateFormat(k, q, C)
            };
            ""
        });
    P(u, "parts/Axis.js", [u["parts/Globals.js"], u["parts/Color.js"], u["parts/Tick.js"], u["parts/Utilities.js"]], function (f, k, H, q) {
        var G = k.parse, M = q.addEvent, L = q.animObject, C = q.arrayMax, F = q.arrayMin, E = q.clamp,
            A = q.correctFloat, v = q.defined, B = q.destroyObjectProperties, y = q.error, p = q.extend,
            l = q.fireEvent,
            n = q.format, e = q.getMagnitude, d = q.isArray, g = q.isFunction, c = q.isNumber, b = q.isString,
            a = q.merge, w = q.normalizeTickInterval, z = q.objectEach, D = q.pick, r = q.relativeLength,
            h = q.removeEvent, t = q.splat, I = q.syncTimeout, J = f.defaultOptions, N = f.deg2rad;
        k = function () {
            this.init.apply(this, arguments)
        };
        p(k.prototype, {
            defaultOptions: {
                dateTimeLabelFormats: {
                    millisecond: {main: "%H:%M:%S.%L", range: !1},
                    second: {main: "%H:%M:%S", range: !1},
                    minute: {main: "%H:%M", range: !1},
                    hour: {main: "%H:%M", range: !1},
                    day: {main: "%e. %b"},
                    week: {main: "%e. %b"},
                    month: {main: "%b '%y"},
                    year: {main: "%Y"}
                },
                endOnTick: !1,
                labels: {
                    enabled: !0,
                    indentation: 10,
                    x: 0,
                    style: {color: "#666666", cursor: "default", fontSize: "11px"}
                },
                maxPadding: .01,
                minorTickLength: 2,
                minorTickPosition: "outside",
                minPadding: .01,
                showEmpty: !0,
                startOfWeek: 1,
                startOnTick: !1,
                tickLength: 10,
                tickPixelInterval: 100,
                tickmarkPlacement: "between",
                tickPosition: "outside",
                title: {align: "middle", style: {color: "#666666"}},
                type: "linear",
                minorGridLineColor: "#f2f2f2",
                minorGridLineWidth: 1,
                minorTickColor: "#999999",
                lineColor: "#ccd6eb",
                lineWidth: 1,
                gridLineColor: "#e6e6e6",
                tickColor: "#ccd6eb"
            },
            defaultYAxisOptions: {
                endOnTick: !0,
                maxPadding: .05,
                minPadding: .05,
                tickPixelInterval: 72,
                showLastLabel: !0,
                labels: {x: -8},
                startOnTick: !0,
                title: {rotation: 270, text: "Values"},
                stackLabels: {
                    allowOverlap: !1, enabled: !1, crop: !0, overflow: "justify", formatter: function () {
                        var a = this.axis.chart.numberFormatter;
                        return a(this.total, -1)
                    }, style: {color: "#000000", fontSize: "11px", fontWeight: "bold", textOutline: "1px contrast"}
                },
                gridLineWidth: 1,
                lineWidth: 0
            },
            defaultLeftAxisOptions: {
                labels: {x: -15},
                title: {rotation: 270}
            },
            defaultRightAxisOptions: {labels: {x: 15}, title: {rotation: 90}},
            defaultBottomAxisOptions: {labels: {autoRotation: [-45], x: 0}, margin: 15, title: {rotation: 0}},
            defaultTopAxisOptions: {labels: {autoRotation: [-45], x: 0}, margin: 15, title: {rotation: 0}},
            init: function (a, b) {
                var m = b.isX, c = this;
                c.chart = a;
                c.horiz = a.inverted && !c.isZAxis ? !m : m;
                c.isXAxis = m;
                c.coll = c.coll || (m ? "xAxis" : "yAxis");
                l(this, "init", {userOptions: b});
                c.opposite = b.opposite;
                c.side = b.side || (c.horiz ? c.opposite ? 0 : 2 : c.opposite ? 1 : 3);
                c.setOptions(b);
                var h = this.options, d = h.type;
                c.labelFormatter = h.labels.formatter || c.defaultLabelFormatter;
                c.userOptions = b;
                c.minPixelPadding = 0;
                c.reversed = h.reversed;
                c.visible = !1 !== h.visible;
                c.zoomEnabled = !1 !== h.zoomEnabled;
                c.hasNames = "category" === d || !0 === h.categories;
                c.categories = h.categories || c.hasNames;
                c.names || (c.names = [], c.names.keys = {});
                c.plotLinesAndBandsGroups = {};
                c.isLog = "logarithmic" === d;
                c.isDatetimeAxis = "datetime" === d;
                c.positiveValuesOnly = c.isLog && !c.allowNegativeLog;
                c.isLinked = v(h.linkedTo);
                c.ticks = {};
                c.labelEdge =
                    [];
                c.minorTicks = {};
                c.plotLinesAndBands = [];
                c.alternateBands = {};
                c.len = 0;
                c.minRange = c.userMinRange = h.minRange || h.maxZoom;
                c.range = h.range;
                c.offset = h.offset || 0;
                c.stacks = {};
                c.oldStacks = {};
                c.stacksTouched = 0;
                c.max = null;
                c.min = null;
                c.crosshair = D(h.crosshair, t(a.options.tooltip.crosshairs)[m ? 0 : 1], !1);
                b = c.options.events;
                -1 === a.axes.indexOf(c) && (m ? a.axes.splice(a.xAxis.length, 0, c) : a.axes.push(c), a[c.coll].push(c));
                c.series = c.series || [];
                a.inverted && !c.isZAxis && m && "undefined" === typeof c.reversed && (c.reversed = !0);
                z(b, function (a, b) {
                    g(a) && M(c, b, a)
                });
                c.lin2log = h.linearToLogConverter || c.lin2log;
                c.isLog && (c.val2lin = c.log2lin, c.lin2val = c.lin2log);
                l(this, "afterInit")
            },
            setOptions: function (b) {
                this.options = a(this.defaultOptions, "yAxis" === this.coll && this.defaultYAxisOptions, [this.defaultTopAxisOptions, this.defaultRightAxisOptions, this.defaultBottomAxisOptions, this.defaultLeftAxisOptions][this.side], a(J[this.coll], b));
                l(this, "afterSetOptions", {userOptions: b})
            },
            defaultLabelFormatter: function () {
                var a = this.axis, b = this.value,
                    c = a.chart.time, h = a.categories, d = this.dateTimeLabelFormat, e = J.lang, g = e.numericSymbols;
                e = e.numericSymbolMagnitude || 1E3;
                var w = g && g.length, r = a.options.labels.format;
                a = a.isLog ? Math.abs(b) : a.tickInterval;
                var t = this.chart, z = t.numberFormatter;
                if (r) var l = n(r, this, t); else if (h) l = b; else if (d) l = c.dateFormat(d, b); else if (w && 1E3 <= a) for (; w-- && "undefined" === typeof l;) c = Math.pow(e, w + 1), a >= c && 0 === 10 * b % c && null !== g[w] && 0 !== b && (l = z(b / c, -1) + g[w]);
                "undefined" === typeof l && (l = 1E4 <= Math.abs(b) ? z(b, -1) : z(b, -1, void 0, ""));
                return l
            },
            getSeriesExtremes: function () {
                var a = this, b = a.chart, h;
                l(this, "getSeriesExtremes", null, function () {
                    a.hasVisibleSeries = !1;
                    a.dataMin = a.dataMax = a.threshold = null;
                    a.softThreshold = !a.isXAxis;
                    a.buildStacks && a.buildStacks();
                    a.series.forEach(function (m) {
                        if (m.visible || !b.options.chart.ignoreHiddenSeries) {
                            var d = m.options, e = d.threshold;
                            a.hasVisibleSeries = !0;
                            a.positiveValuesOnly && 0 >= e && (e = null);
                            if (a.isXAxis) {
                                if (d = m.xData, d.length) {
                                    h = m.getXExtremes(d);
                                    var x = h.min;
                                    var g = h.max;
                                    c(x) || x instanceof Date || (d = d.filter(c),
                                        h = m.getXExtremes(d), x = h.min, g = h.max);
                                    d.length && (a.dataMin = Math.min(D(a.dataMin, x), x), a.dataMax = Math.max(D(a.dataMax, g), g))
                                }
                            } else if (m.getExtremes(), g = m.dataMax, x = m.dataMin, v(x) && v(g) && (a.dataMin = Math.min(D(a.dataMin, x), x), a.dataMax = Math.max(D(a.dataMax, g), g)), v(e) && (a.threshold = e), !d.softThreshold || a.positiveValuesOnly) a.softThreshold = !1
                        }
                    })
                });
                l(this, "afterGetSeriesExtremes")
            },
            translate: function (a, b, h, d, e, g) {
                var m = this.linkedParent || this, x = 1, w = 0, r = d ? m.oldTransA : m.transA;
                d = d ? m.oldMin : m.min;
                var K = m.minPixelPadding;
                e = (m.isOrdinal || m.isBroken || m.isLog && e) && m.lin2val;
                r || (r = m.transA);
                h && (x *= -1, w = m.len);
                m.reversed && (x *= -1, w -= x * (m.sector || m.len));
                b ? (a = (a * x + w - K) / r + d, e && (a = m.lin2val(a))) : (e && (a = m.val2lin(a)), a = c(d) ? x * (a - d) * r + w + x * K + (c(g) ? r * g : 0) : void 0);
                return a
            },
            toPixels: function (a, b) {
                return this.translate(a, !1, !this.horiz, null, !0) + (b ? 0 : this.pos)
            },
            toValue: function (a, b) {
                return this.translate(a - (b ? 0 : this.pos), !0, !this.horiz, null, !0)
            },
            getPlotLinePath: function (a) {
                var b = this, h = b.chart, d = b.left, e = b.top, x = a.old, g = a.value,
                    w = a.translatedValue, r = a.lineWidth, t = a.force, z, I, n, J,
                    p = x && h.oldChartHeight || h.chartHeight, f = x && h.oldChartWidth || h.chartWidth, N,
                    v = b.transB, y = function (a, b, m) {
                        if ("pass" !== t && a < b || a > m) t ? a = E(a, b, m) : N = !0;
                        return a
                    };
                a = {value: g, lineWidth: r, old: x, force: t, acrossPanes: a.acrossPanes, translatedValue: w};
                l(this, "getPlotLinePath", a, function (a) {
                    w = D(w, b.translate(g, null, null, x));
                    w = E(w, -1E5, 1E5);
                    z = n = Math.round(w + v);
                    I = J = Math.round(p - w - v);
                    c(w) ? b.horiz ? (I = e, J = p - b.bottom, z = n = y(z, d, d + b.width)) : (z = d, n = f - b.right, I = J = y(I, e, e +
                        b.height)) : (N = !0, t = !1);
                    a.path = N && !t ? null : h.renderer.crispLine(["M", z, I, "L", n, J], r || 1)
                });
                return a.path
            },
            getLinearTickPositions: function (a, b, c) {
                var m = A(Math.floor(b / a) * a);
                c = A(Math.ceil(c / a) * a);
                var h = [], d;
                A(m + a) === m && (d = 20);
                if (this.single) return [b];
                for (b = m; b <= c;) {
                    h.push(b);
                    b = A(b + a, d);
                    if (b === e) break;
                    var e = b
                }
                return h
            },
            getMinorTickInterval: function () {
                var a = this.options;
                return !0 === a.minorTicks ? D(a.minorTickInterval, "auto") : !1 === a.minorTicks ? null : a.minorTickInterval
            },
            getMinorTickPositions: function () {
                var a =
                        this, b = a.options, c = a.tickPositions, h = a.minorTickInterval, d = [],
                    e = a.pointRangePadding || 0, g = a.min - e;
                e = a.max + e;
                var w = e - g;
                if (w && w / h < a.len / 3) if (a.isLog) this.paddedTicks.forEach(function (b, m, c) {
                    m && d.push.apply(d, a.getLogTickPositions(h, c[m - 1], c[m], !0))
                }); else if (a.isDatetimeAxis && "auto" === this.getMinorTickInterval()) d = d.concat(a.getTimeTicks(a.normalizeTimeTickInterval(h), g, e, b.startOfWeek)); else for (b = g + (c[0] - g) % h; b <= e && b !== d[0]; b += h) d.push(b);
                0 !== d.length && a.trimTicks(d);
                return d
            },
            adjustForMinRange: function () {
                var a =
                    this.options, b = this.min, c = this.max, h, d, e, g, w;
                this.isXAxis && "undefined" === typeof this.minRange && !this.isLog && (v(a.min) || v(a.max) ? this.minRange = null : (this.series.forEach(function (a) {
                    g = a.xData;
                    for (d = w = a.xIncrement ? 1 : g.length - 1; 0 < d; d--) if (e = g[d] - g[d - 1], "undefined" === typeof h || e < h) h = e
                }), this.minRange = Math.min(5 * h, this.dataMax - this.dataMin)));
                if (c - b < this.minRange) {
                    var r = this.dataMax - this.dataMin >= this.minRange;
                    var t = this.minRange;
                    var z = (t - c + b) / 2;
                    z = [b - z, D(a.min, b - z)];
                    r && (z[2] = this.isLog ? this.log2lin(this.dataMin) :
                        this.dataMin);
                    b = C(z);
                    c = [b + t, D(a.max, b + t)];
                    r && (c[2] = this.isLog ? this.log2lin(this.dataMax) : this.dataMax);
                    c = F(c);
                    c - b < t && (z[0] = c - t, z[1] = D(a.min, c - t), b = C(z))
                }
                this.min = b;
                this.max = c
            },
            getClosest: function () {
                var a;
                this.categories ? a = 1 : this.series.forEach(function (b) {
                    var m = b.closestPointRange, c = b.visible || !b.chart.options.chart.ignoreHiddenSeries;
                    !b.noSharedTooltip && v(m) && c && (a = v(a) ? Math.min(a, m) : m)
                });
                return a
            },
            nameToX: function (a) {
                var b = d(this.categories), c = b ? this.categories : this.names, h = a.options.x;
                a.series.requireSorting =
                    !1;
                v(h) || (h = !1 === this.options.uniqueNames ? a.series.autoIncrement() : b ? c.indexOf(a.name) : D(c.keys[a.name], -1));
                if (-1 === h) {
                    if (!b) var e = c.length
                } else e = h;
                "undefined" !== typeof e && (this.names[e] = a.name, this.names.keys[a.name] = e);
                return e
            },
            updateNames: function () {
                var a = this, b = this.names;
                0 < b.length && (Object.keys(b.keys).forEach(function (a) {
                    delete b.keys[a]
                }), b.length = 0, this.minRange = this.userMinRange, (this.series || []).forEach(function (b) {
                    b.xIncrement = null;
                    if (!b.points || b.isDirtyData) a.max = Math.max(a.max, b.xData.length -
                        1), b.processData(), b.generatePoints();
                    b.data.forEach(function (m, c) {
                        if (m && m.options && "undefined" !== typeof m.name) {
                            var h = a.nameToX(m);
                            "undefined" !== typeof h && h !== m.x && (m.x = h, b.xData[c] = h)
                        }
                    })
                }))
            },
            setAxisTranslation: function (a) {
                var m = this, c = m.max - m.min, h = m.axisPointRange || 0, d = 0, e = 0, g = m.linkedParent,
                    x = !!m.categories, w = m.transA, r = m.isXAxis;
                if (r || x || h) {
                    var t = m.getClosest();
                    g ? (d = g.minPointOffset, e = g.pointRangePadding) : m.series.forEach(function (a) {
                        var c = x ? 1 : r ? D(a.options.pointRange, t, 0) : m.axisPointRange ||
                            0, g = a.options.pointPlacement;
                        h = Math.max(h, c);
                        if (!m.single || x) a = a.is("xrange") ? !r : r, d = Math.max(d, a && b(g) ? 0 : c / 2), e = Math.max(e, a && "on" === g ? 0 : c)
                    });
                    g = m.ordinalSlope && t ? m.ordinalSlope / t : 1;
                    m.minPointOffset = d *= g;
                    m.pointRangePadding = e *= g;
                    m.pointRange = Math.min(h, m.single && x ? 1 : c);
                    r && (m.closestPointRange = t)
                }
                a && (m.oldTransA = w);
                m.translationSlope = m.transA = w = m.staticScale || m.len / (c + e || 1);
                m.transB = m.horiz ? m.left : m.bottom;
                m.minPixelPadding = w * d;
                l(this, "afterSetAxisTranslation")
            },
            minFromRange: function () {
                return this.max -
                    this.range
            },
            setTickInterval: function (a) {
                var b = this, h = b.chart, d = b.options, g = b.isLog, x = b.isDatetimeAxis, r = b.isXAxis,
                    t = b.isLinked, z = d.maxPadding, I = d.minPadding, n = d.tickInterval, J = d.tickPixelInterval,
                    p = b.categories, f = c(b.threshold) ? b.threshold : null, N = b.softThreshold;
                x || p || t || this.getTickAmount();
                var k = D(b.userMin, d.min);
                var B = D(b.userMax, d.max);
                if (t) {
                    b.linkedParent = h[b.coll][d.linkedTo];
                    var q = b.linkedParent.getExtremes();
                    b.min = D(q.min, q.dataMin);
                    b.max = D(q.max, q.dataMax);
                    d.type !== b.linkedParent.options.type &&
                    y(11, 1, h)
                } else {
                    if (!N && v(f)) if (b.dataMin >= f) q = f, I = 0; else if (b.dataMax <= f) {
                        var E = f;
                        z = 0
                    }
                    b.min = D(k, q, b.dataMin);
                    b.max = D(B, E, b.dataMax)
                }
                g && (b.positiveValuesOnly && !a && 0 >= Math.min(b.min, D(b.dataMin, b.min)) && y(10, 1, h), b.min = A(b.log2lin(b.min), 16), b.max = A(b.log2lin(b.max), 16));
                b.range && v(b.max) && (b.userMin = b.min = k = Math.max(b.dataMin, b.minFromRange()), b.userMax = B = b.max, b.range = null);
                l(b, "foundExtremes");
                b.beforePadding && b.beforePadding();
                b.adjustForMinRange();
                !(p || b.axisPointRange || b.usePercentage || t) && v(b.min) &&
                v(b.max) && (h = b.max - b.min) && (!v(k) && I && (b.min -= h * I), !v(B) && z && (b.max += h * z));
                c(b.userMin) || (c(d.softMin) && d.softMin < b.min && (b.min = k = d.softMin), c(d.floor) && (b.min = Math.max(b.min, d.floor)));
                c(b.userMax) || (c(d.softMax) && d.softMax > b.max && (b.max = B = d.softMax), c(d.ceiling) && (b.max = Math.min(b.max, d.ceiling)));
                N && v(b.dataMin) && (f = f || 0, !v(k) && b.min < f && b.dataMin >= f ? b.min = b.options.minRange ? Math.min(f, b.max - b.minRange) : f : !v(B) && b.max > f && b.dataMax <= f && (b.max = b.options.minRange ? Math.max(f, b.min + b.minRange) : f));
                b.tickInterval =
                    b.min === b.max || "undefined" === typeof b.min || "undefined" === typeof b.max ? 1 : t && !n && J === b.linkedParent.options.tickPixelInterval ? n = b.linkedParent.tickInterval : D(n, this.tickAmount ? (b.max - b.min) / Math.max(this.tickAmount - 1, 1) : void 0, p ? 1 : (b.max - b.min) * J / Math.max(b.len, J));
                r && !a && b.series.forEach(function (a) {
                    a.processData(b.min !== b.oldMin || b.max !== b.oldMax)
                });
                b.setAxisTranslation(!0);
                b.beforeSetTickPositions && b.beforeSetTickPositions();
                b.postProcessTickInterval && (b.tickInterval = b.postProcessTickInterval(b.tickInterval));
                b.pointRange && !n && (b.tickInterval = Math.max(b.pointRange, b.tickInterval));
                a = D(d.minTickInterval, b.isDatetimeAxis && b.closestPointRange);
                !n && b.tickInterval < a && (b.tickInterval = a);
                x || g || n || (b.tickInterval = w(b.tickInterval, null, e(b.tickInterval), D(d.allowDecimals, !(.5 < b.tickInterval && 5 > b.tickInterval && 1E3 < b.max && 9999 > b.max)), !!this.tickAmount));
                this.tickAmount || (b.tickInterval = b.unsquish());
                this.setTickPositions()
            },
            setTickPositions: function () {
                var a = this.options, b = a.tickPositions;
                var c = this.getMinorTickInterval();
                var h = a.tickPositioner, d = a.startOnTick, e = a.endOnTick;
                this.tickmarkOffset = this.categories && "between" === a.tickmarkPlacement && 1 === this.tickInterval ? .5 : 0;
                this.minorTickInterval = "auto" === c && this.tickInterval ? this.tickInterval / 5 : c;
                this.single = this.min === this.max && v(this.min) && !this.tickAmount && (parseInt(this.min, 10) === this.min || !1 !== a.allowDecimals);
                this.tickPositions = c = b && b.slice();
                !c && (!this.ordinalPositions && (this.max - this.min) / this.tickInterval > Math.max(2 * this.len, 200) ? (c = [this.min, this.max], y(19, !1,
                    this.chart)) : c = this.isDatetimeAxis ? this.getTimeTicks(this.normalizeTimeTickInterval(this.tickInterval, a.units), this.min, this.max, a.startOfWeek, this.ordinalPositions, this.closestPointRange, !0) : this.isLog ? this.getLogTickPositions(this.tickInterval, this.min, this.max) : this.getLinearTickPositions(this.tickInterval, this.min, this.max), c.length > this.len && (c = [c[0], c.pop()], c[0] === c[1] && (c.length = 1)), this.tickPositions = c, h && (h = h.apply(this, [this.min, this.max]))) && (this.tickPositions = c = h);
                this.paddedTicks = c.slice(0);
                this.trimTicks(c, d, e);
                this.isLinked || (this.single && 2 > c.length && !this.categories && !this.series.some(function (a) {
                    return a.is("heatmap") && "between" === a.options.pointPlacement
                }) && (this.min -= .5, this.max += .5), b || h || this.adjustTickAmount());
                l(this, "afterSetTickPositions")
            },
            trimTicks: function (a, b, c) {
                var m = a[0], h = a[a.length - 1], d = !this.isOrdinal && this.minPointOffset || 0;
                l(this, "trimTicks");
                if (!this.isLinked) {
                    if (b && -Infinity !== m) this.min = m; else for (; this.min - d > a[0];) a.shift();
                    if (c) this.max = h; else for (; this.max +
                                                     d < a[a.length - 1];) a.pop();
                    0 === a.length && v(m) && !this.options.tickPositions && a.push((h + m) / 2)
                }
            },
            alignToOthers: function () {
                var a = {}, b, c = this.options;
                !1 === this.chart.options.chart.alignTicks || !1 === c.alignTicks || !1 === c.startOnTick || !1 === c.endOnTick || this.isLog || this.chart[this.coll].forEach(function (c) {
                    var m = c.options;
                    m = [c.horiz ? m.left : m.top, m.width, m.height, m.pane].join();
                    c.series.length && (a[m] ? b = !0 : a[m] = 1)
                });
                return b
            },
            getTickAmount: function () {
                var a = this.options, b = a.tickAmount, c = a.tickPixelInterval;
                !v(a.tickInterval) &&
                this.len < c && !this.isRadial && !this.isLog && a.startOnTick && a.endOnTick && (b = 2);
                !b && this.alignToOthers() && (b = Math.ceil(this.len / c) + 1);
                4 > b && (this.finalTickAmt = b, b = 5);
                this.tickAmount = b
            },
            adjustTickAmount: function () {
                var a = this.options, b = this.tickInterval, c = this.tickPositions, h = this.tickAmount,
                    d = this.finalTickAmt, e = c && c.length, g = D(this.threshold, this.softThreshold ? 0 : null), w;
                if (this.hasData()) {
                    if (e < h) {
                        for (w = this.min; c.length < h;) c.length % 2 || w === g ? c.push(A(c[c.length - 1] + b)) : c.unshift(A(c[0] - b));
                        this.transA *= (e -
                            1) / (h - 1);
                        this.min = a.startOnTick ? c[0] : Math.min(this.min, c[0]);
                        this.max = a.endOnTick ? c[c.length - 1] : Math.max(this.max, c[c.length - 1])
                    } else e > h && (this.tickInterval *= 2, this.setTickPositions());
                    if (v(d)) {
                        for (b = a = c.length; b--;) (3 === d && 1 === b % 2 || 2 >= d && 0 < b && b < a - 1) && c.splice(b, 1);
                        this.finalTickAmt = void 0
                    }
                }
            },
            setScale: function () {
                var a = this.series.some(function (a) {
                    return a.isDirtyData || a.isDirty || a.xAxis && a.xAxis.isDirty
                }), b;
                this.oldMin = this.min;
                this.oldMax = this.max;
                this.oldAxisLength = this.len;
                this.setAxisSize();
                (b = this.len !== this.oldAxisLength) || a || this.isLinked || this.forceRedraw || this.userMin !== this.oldUserMin || this.userMax !== this.oldUserMax || this.alignToOthers() ? (this.resetStacks && this.resetStacks(), this.forceRedraw = !1, this.getSeriesExtremes(), this.setTickInterval(), this.oldUserMin = this.userMin, this.oldUserMax = this.userMax, this.isDirty || (this.isDirty = b || this.min !== this.oldMin || this.max !== this.oldMax)) : this.cleanStacks && this.cleanStacks();
                l(this, "afterSetScale")
            },
            setExtremes: function (a, b, c, h, d) {
                var m = this,
                    e = m.chart;
                c = D(c, !0);
                m.series.forEach(function (a) {
                    delete a.kdTree
                });
                d = p(d, {min: a, max: b});
                l(m, "setExtremes", d, function () {
                    m.userMin = a;
                    m.userMax = b;
                    m.eventArgs = d;
                    c && e.redraw(h)
                })
            },
            zoom: function (a, b) {
                var c = this.dataMin, m = this.dataMax, h = this.options, d = Math.min(c, D(h.min, c)),
                    e = Math.max(m, D(h.max, m));
                a = {newMin: a, newMax: b};
                l(this, "zoom", a, function (a) {
                    var b = a.newMin, h = a.newMax;
                    if (b !== this.min || h !== this.max) this.allowZoomOutside || (v(c) && (b < d && (b = d), b > e && (b = e)), v(m) && (h < d && (h = d), h > e && (h = e))), this.displayBtn = "undefined" !==
                        typeof b || "undefined" !== typeof h, this.setExtremes(b, h, !1, void 0, {trigger: "zoom"});
                    a.zoomed = !0
                });
                return a.zoomed
            },
            setAxisSize: function () {
                var a = this.chart, b = this.options, c = b.offsets || [0, 0, 0, 0], h = this.horiz,
                    d = this.width = Math.round(r(D(b.width, a.plotWidth - c[3] + c[1]), a.plotWidth)),
                    e = this.height = Math.round(r(D(b.height, a.plotHeight - c[0] + c[2]), a.plotHeight)),
                    g = this.top = Math.round(r(D(b.top, a.plotTop + c[0]), a.plotHeight, a.plotTop));
                b = this.left = Math.round(r(D(b.left, a.plotLeft + c[3]), a.plotWidth, a.plotLeft));
                this.bottom = a.chartHeight - e - g;
                this.right = a.chartWidth - d - b;
                this.len = Math.max(h ? d : e, 0);
                this.pos = h ? b : g
            },
            getExtremes: function () {
                var a = this.isLog;
                return {
                    min: a ? A(this.lin2log(this.min)) : this.min,
                    max: a ? A(this.lin2log(this.max)) : this.max,
                    dataMin: this.dataMin,
                    dataMax: this.dataMax,
                    userMin: this.userMin,
                    userMax: this.userMax
                }
            },
            getThreshold: function (a) {
                var b = this.isLog, c = b ? this.lin2log(this.min) : this.min;
                b = b ? this.lin2log(this.max) : this.max;
                null === a || -Infinity === a ? a = c : Infinity === a ? a = b : c > a ? a = c : b < a && (a = b);
                return this.translate(a,
                    0, 1, 0, 1)
            },
            autoLabelAlign: function (a) {
                var b = (D(a, 0) - 90 * this.side + 720) % 360;
                a = {align: "center"};
                l(this, "autoLabelAlign", a, function (a) {
                    15 < b && 165 > b ? a.align = "right" : 195 < b && 345 > b && (a.align = "left")
                });
                return a.align
            },
            tickSize: function (a) {
                var b = this.options, c = b[a + "Length"],
                    h = D(b[a + "Width"], "tick" === a && this.isXAxis && !this.categories ? 1 : 0);
                if (h && c) {
                    "inside" === b[a + "Position"] && (c = -c);
                    var d = [c, h]
                }
                a = {tickSize: d};
                l(this, "afterTickSize", a);
                return a.tickSize
            },
            labelMetrics: function () {
                var a = this.tickPositions && this.tickPositions[0] ||
                    0;
                return this.chart.renderer.fontMetrics(this.options.labels.style && this.options.labels.style.fontSize, this.ticks[a] && this.ticks[a].label)
            },
            unsquish: function () {
                var a = this.options.labels, b = this.horiz, c = this.tickInterval, h = c,
                    d = this.len / (((this.categories ? 1 : 0) + this.max - this.min) / c), e, g = a.rotation,
                    w = this.labelMetrics(), r, t = Number.MAX_VALUE, z, l = this.max - this.min, I = function (a) {
                        var b = a / (d || 1);
                        b = 1 < b ? Math.ceil(b) : 1;
                        b * c > l && Infinity !== a && Infinity !== d && l && (b = Math.ceil(l / c));
                        return A(b * c)
                    };
                b ? (z = !a.staggerLines &&
                    !a.step && (v(g) ? [g] : d < D(a.autoRotationLimit, 80) && a.autoRotation)) && z.forEach(function (a) {
                    if (a === g || a && -90 <= a && 90 >= a) {
                        r = I(Math.abs(w.h / Math.sin(N * a)));
                        var b = r + Math.abs(a / 360);
                        b < t && (t = b, e = a, h = r)
                    }
                }) : a.step || (h = I(w.h));
                this.autoRotation = z;
                this.labelRotation = D(e, g);
                return h
            },
            getSlotWidth: function (a) {
                var b = this.chart, c = this.horiz, h = this.options.labels,
                    d = Math.max(this.tickPositions.length - (this.categories ? 0 : 1), 1), e = b.margin[3];
                return a && a.slotWidth || c && 2 > (h.step || 0) && !h.rotation && (this.staggerLines || 1) * this.len /
                    d || !c && (h.style && parseInt(h.style.width, 10) || e && e - b.spacing[3] || .33 * b.chartWidth)
            },
            renderUnsquish: function () {
                var a = this.chart, c = a.renderer, h = this.tickPositions, d = this.ticks, e = this.options.labels,
                    g = e && e.style || {}, w = this.horiz, r = this.getSlotWidth(),
                    t = Math.max(1, Math.round(r - 2 * (e.padding || 5))), z = {}, l = this.labelMetrics(),
                    D = e.style && e.style.textOverflow, I = 0;
                b(e.rotation) || (z.rotation = e.rotation || 0);
                h.forEach(function (a) {
                    a = d[a];
                    a.movedLabel && a.replaceMovedLabel();
                    a && a.label && a.label.textPxLength > I && (I =
                        a.label.textPxLength)
                });
                this.maxLabelLength = I;
                if (this.autoRotation) I > t && I > l.h ? z.rotation = this.labelRotation : this.labelRotation = 0; else if (r) {
                    var n = t;
                    if (!D) {
                        var J = "clip";
                        for (t = h.length; !w && t--;) {
                            var p = h[t];
                            if (p = d[p].label) p.styles && "ellipsis" === p.styles.textOverflow ? p.css({textOverflow: "clip"}) : p.textPxLength > r && p.css({width: r + "px"}), p.getBBox().height > this.len / h.length - (l.h - l.f) && (p.specificTextOverflow = "ellipsis")
                        }
                    }
                }
                z.rotation && (n = I > .5 * a.chartHeight ? .33 * a.chartHeight : I, D || (J = "ellipsis"));
                if (this.labelAlign =
                    e.align || this.autoLabelAlign(this.labelRotation)) z.align = this.labelAlign;
                h.forEach(function (a) {
                    var b = (a = d[a]) && a.label, c = g.width, m = {};
                    b && (b.attr(z), a.shortenLabel ? a.shortenLabel() : n && !c && "nowrap" !== g.whiteSpace && (n < b.textPxLength || "SPAN" === b.element.tagName) ? (m.width = n, D || (m.textOverflow = b.specificTextOverflow || J), b.css(m)) : b.styles && b.styles.width && !m.width && !c && b.css({width: null}), delete b.specificTextOverflow, a.rotation = z.rotation)
                }, this);
                this.tickRotCorr = c.rotCorr(l.b, this.labelRotation || 0, 0 !==
                    this.side)
            },
            hasData: function () {
                return this.series.some(function (a) {
                    return a.hasData()
                }) || this.options.showEmpty && v(this.min) && v(this.max)
            },
            addTitle: function (b) {
                var c = this.chart.renderer, h = this.horiz, d = this.opposite, e = this.options.title, g,
                    w = this.chart.styledMode;
                this.axisTitle || ((g = e.textAlign) || (g = (h ? {
                    low: "left",
                    middle: "center",
                    high: "right"
                } : {
                    low: d ? "right" : "left",
                    middle: "center",
                    high: d ? "left" : "right"
                })[e.align]), this.axisTitle = c.text(e.text, 0, 0, e.useHTML).attr({
                    zIndex: 7,
                    rotation: e.rotation || 0,
                    align: g
                }).addClass("highcharts-axis-title"),
                w || this.axisTitle.css(a(e.style)), this.axisTitle.add(this.axisGroup), this.axisTitle.isNew = !0);
                w || e.style.width || this.isRadial || this.axisTitle.css({width: this.len});
                this.axisTitle[b ? "show" : "hide"](b)
            },
            generateTick: function (a) {
                var b = this.ticks;
                b[a] ? b[a].addLabel() : b[a] = new H(this, a)
            },
            getOffset: function () {
                var a = this, b = a.chart, c = b.renderer, h = a.options, d = a.tickPositions, e = a.ticks, g = a.horiz,
                    w = a.side, r = b.inverted && !a.isZAxis ? [1, 0, 3, 2][w] : w, t, I = 0, n = 0, J = h.title,
                    p = h.labels, f = 0, N = b.axisOffset;
                b = b.clipOffset;
                var y = [-1, 1, 1, -1][w], A = h.className, k = a.axisParent;
                var B = a.hasData();
                a.showAxis = t = B || D(h.showEmpty, !0);
                a.staggerLines = a.horiz && p.staggerLines;
                a.axisGroup || (a.gridGroup = c.g("grid").attr({zIndex: h.gridZIndex || 1}).addClass("highcharts-" + this.coll.toLowerCase() + "-grid " + (A || "")).add(k), a.axisGroup = c.g("axis").attr({zIndex: h.zIndex || 2}).addClass("highcharts-" + this.coll.toLowerCase() + " " + (A || "")).add(k), a.labelGroup = c.g("axis-labels").attr({zIndex: p.zIndex || 7}).addClass("highcharts-" + a.coll.toLowerCase() +
                    "-labels " + (A || "")).add(k));
                B || a.isLinked ? (d.forEach(function (b, c) {
                    a.generateTick(b, c)
                }), a.renderUnsquish(), a.reserveSpaceDefault = 0 === w || 2 === w || {
                    1: "left",
                    3: "right"
                }[w] === a.labelAlign, D(p.reserveSpace, "center" === a.labelAlign ? !0 : null, a.reserveSpaceDefault) && d.forEach(function (a) {
                    f = Math.max(e[a].getLabelSize(), f)
                }), a.staggerLines && (f *= a.staggerLines), a.labelOffset = f * (a.opposite ? -1 : 1)) : z(e, function (a, b) {
                    a.destroy();
                    delete e[b]
                });
                if (J && J.text && !1 !== J.enabled && (a.addTitle(t), t && !1 !== J.reserveSpace)) {
                    a.titleOffset =
                        I = a.axisTitle.getBBox()[g ? "height" : "width"];
                    var q = J.offset;
                    n = v(q) ? 0 : D(J.margin, g ? 5 : 10)
                }
                a.renderLine();
                a.offset = y * D(h.offset, N[w] ? N[w] + (h.margin || 0) : 0);
                a.tickRotCorr = a.tickRotCorr || {x: 0, y: 0};
                c = 0 === w ? -a.labelMetrics().h : 2 === w ? a.tickRotCorr.y : 0;
                n = Math.abs(f) + n;
                f && (n = n - c + y * (g ? D(p.y, a.tickRotCorr.y + 8 * y) : p.x));
                a.axisTitleMargin = D(q, n);
                a.getMaxLabelDimensions && (a.maxLabelDimensions = a.getMaxLabelDimensions(e, d));
                g = this.tickSize("tick");
                N[w] = Math.max(N[w], a.axisTitleMargin + I + y * a.offset, n, d && d.length && g ? g[0] +
                    y * a.offset : 0);
                h = h.offset ? 0 : 2 * Math.floor(a.axisLine.strokeWidth() / 2);
                b[r] = Math.max(b[r], h);
                l(this, "afterGetOffset")
            },
            getLinePath: function (a) {
                var b = this.chart, c = this.opposite, h = this.offset, d = this.horiz,
                    e = this.left + (c ? this.width : 0) + h;
                h = b.chartHeight - this.bottom - (c ? this.height : 0) + h;
                c && (a *= -1);
                return b.renderer.crispLine(["M", d ? this.left : e, d ? h : this.top, "L", d ? b.chartWidth - this.right : e, d ? h : b.chartHeight - this.bottom], a)
            },
            renderLine: function () {
                this.axisLine || (this.axisLine = this.chart.renderer.path().addClass("highcharts-axis-line").add(this.axisGroup),
                this.chart.styledMode || this.axisLine.attr({
                    stroke: this.options.lineColor,
                    "stroke-width": this.options.lineWidth,
                    zIndex: 7
                }))
            },
            getTitlePosition: function () {
                var a = this.horiz, b = this.left, c = this.top, h = this.len, d = this.options.title, e = a ? b : c,
                    g = this.opposite, w = this.offset, r = d.x || 0, t = d.y || 0, z = this.axisTitle,
                    I = this.chart.renderer.fontMetrics(d.style && d.style.fontSize, z);
                z = Math.max(z.getBBox(null, 0).height - I.h - 1, 0);
                h = {low: e + (a ? 0 : h), middle: e + h / 2, high: e + (a ? h : 0)}[d.align];
                b = (a ? c + this.height : b) + (a ? 1 : -1) * (g ? -1 : 1) * this.axisTitleMargin +
                    [-z, z, I.f, -z][this.side];
                a = {x: a ? h + r : b + (g ? this.width : 0) + w + r, y: a ? b + t - (g ? this.height : 0) + w : h + t};
                l(this, "afterGetTitlePosition", {titlePosition: a});
                return a
            },
            renderMinorTick: function (a) {
                var b = this.chart.hasRendered && c(this.oldMin), h = this.minorTicks;
                h[a] || (h[a] = new H(this, a, "minor"));
                b && h[a].isNew && h[a].render(null, !0);
                h[a].render(null, !1, 1)
            },
            renderTick: function (a, b) {
                var h = this.isLinked, m = this.ticks, d = this.chart.hasRendered && c(this.oldMin);
                if (!h || a >= this.min && a <= this.max) m[a] || (m[a] = new H(this, a)), d && m[a].isNew &&
                m[a].render(b, !0, -1), m[a].render(b)
            },
            render: function () {
                var a = this, b = a.chart, h = a.options, d = a.isLog, e = a.isLinked, g = a.tickPositions,
                    w = a.axisTitle, r = a.ticks, t = a.minorTicks, D = a.alternateBands, n = h.stackLabels,
                    J = h.alternateGridColor, p = a.tickmarkOffset, N = a.axisLine, v = a.showAxis,
                    y = L(b.renderer.globalAnimation), A, k;
                a.labelEdge.length = 0;
                a.overlap = !1;
                [r, t, D].forEach(function (a) {
                    z(a, function (a) {
                        a.isActive = !1
                    })
                });
                if (a.hasData() || e) a.minorTickInterval && !a.categories && a.getMinorTickPositions().forEach(function (b) {
                    a.renderMinorTick(b)
                }),
                g.length && (g.forEach(function (b, c) {
                    a.renderTick(b, c)
                }), p && (0 === a.min || a.single) && (r[-1] || (r[-1] = new H(a, -1, null, !0)), r[-1].render(-1))), J && g.forEach(function (c, h) {
                    k = "undefined" !== typeof g[h + 1] ? g[h + 1] + p : a.max - p;
                    0 === h % 2 && c < a.max && k <= a.max + (b.polar ? -p : p) && (D[c] || (D[c] = new f.PlotLineOrBand(a)), A = c + p, D[c].options = {
                        from: d ? a.lin2log(A) : A,
                        to: d ? a.lin2log(k) : k,
                        color: J
                    }, D[c].render(), D[c].isActive = !0)
                }), a._addedPlotLB || ((h.plotLines || []).concat(h.plotBands || []).forEach(function (b) {
                    a.addPlotBandOrLine(b)
                }), a._addedPlotLB =
                    !0);
                [r, t, D].forEach(function (a) {
                    var c, h = [], m = y.duration;
                    z(a, function (a, b) {
                        a.isActive || (a.render(b, !1, 0), a.isActive = !1, h.push(b))
                    });
                    I(function () {
                        for (c = h.length; c--;) a[h[c]] && !a[h[c]].isActive && (a[h[c]].destroy(), delete a[h[c]])
                    }, a !== D && b.hasRendered && m ? m : 0)
                });
                N && (N[N.isPlaced ? "animate" : "attr"]({d: this.getLinePath(N.strokeWidth())}), N.isPlaced = !0, N[v ? "show" : "hide"](v));
                w && v && (h = a.getTitlePosition(), c(h.y) ? (w[w.isNew ? "attr" : "animate"](h), w.isNew = !1) : (w.attr("y", -9999), w.isNew = !0));
                n && n.enabled && a.renderStackTotals();
                a.isDirty = !1;
                l(this, "afterRender")
            },
            redraw: function () {
                this.visible && (this.render(), this.plotLinesAndBands.forEach(function (a) {
                    a.render()
                }));
                this.series.forEach(function (a) {
                    a.isDirty = !0
                })
            },
            keepProps: "extKey hcEvents names series userMax userMin".split(" "),
            destroy: function (a) {
                var b = this, c = b.stacks, d = b.plotLinesAndBands, e;
                l(this, "destroy", {keepEvents: a});
                a || h(b);
                z(c, function (a, b) {
                    B(a);
                    c[b] = null
                });
                [b.ticks, b.minorTicks, b.alternateBands].forEach(function (a) {
                    B(a)
                });
                if (d) for (a = d.length; a--;) d[a].destroy();
                "stackTotalGroup axisLine axisTitle axisGroup gridGroup labelGroup cross scrollbar".split(" ").forEach(function (a) {
                    b[a] && (b[a] = b[a].destroy())
                });
                for (e in b.plotLinesAndBandsGroups) b.plotLinesAndBandsGroups[e] = b.plotLinesAndBandsGroups[e].destroy();
                z(b, function (a, c) {
                    -1 === b.keepProps.indexOf(c) && delete b[c]
                })
            },
            drawCrosshair: function (a, b) {
                var c = this.crosshair, h = D(c.snap, !0), d, m = this.cross, e = this.chart;
                l(this, "drawCrosshair", {e: a, point: b});
                a || (a = this.cross && this.cross.e);
                if (this.crosshair && !1 !== (v(b) ||
                    !h)) {
                    h ? v(b) && (d = D("colorAxis" !== this.coll ? b.crosshairPos : null, this.isXAxis ? b.plotX : this.len - b.plotY)) : d = a && (this.horiz ? a.chartX - this.pos : this.len - a.chartY + this.pos);
                    if (v(d)) {
                        var g = {value: b && (this.isXAxis ? b.x : D(b.stackY, b.y)), translatedValue: d};
                        e.polar && p(g, {isCrosshair: !0, chartX: a && a.chartX, chartY: a && a.chartY, point: b});
                        g = this.getPlotLinePath(g) || null
                    }
                    if (!v(g)) {
                        this.hideCrosshair();
                        return
                    }
                    h = this.categories && !this.isRadial;
                    m || (this.cross = m = e.renderer.path().addClass("highcharts-crosshair highcharts-crosshair-" +
                        (h ? "category " : "thin ") + c.className).attr({zIndex: D(c.zIndex, 2)}).add(), e.styledMode || (m.attr({
                        stroke: c.color || (h ? G("#ccd6eb").setOpacity(.25).get() : "#cccccc"),
                        "stroke-width": D(c.width, 1)
                    }).css({"pointer-events": "none"}), c.dashStyle && m.attr({dashstyle: c.dashStyle})));
                    m.show().attr({d: g});
                    h && !c.width && m.attr({"stroke-width": this.transA});
                    this.cross.e = a
                } else this.hideCrosshair();
                l(this, "afterDrawCrosshair", {e: a, point: b})
            },
            hideCrosshair: function () {
                this.cross && this.cross.hide();
                l(this, "afterHideCrosshair")
            }
        });
        return f.Axis = k
    });
    P(u, "parts/DateTimeAxis.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.getMagnitude, q = k.normalizeTickInterval, G = k.timeUnits;
        f = f.Axis;
        f.prototype.getTimeTicks = function () {
            return this.chart.time.getTimeTicks.apply(this.chart.time, arguments)
        };
        f.prototype.normalizeTimeTickInterval = function (f, k) {
            var C = k || [["millisecond", [1, 2, 5, 10, 20, 25, 50, 100, 200, 500]], ["second", [1, 2, 5, 10, 15, 30]], ["minute", [1, 2, 5, 10, 15, 30]], ["hour", [1, 2, 3, 4, 6, 8, 12]], ["day", [1, 2]], ["week", [1,
                2]], ["month", [1, 2, 3, 4, 6]], ["year", null]];
            k = C[C.length - 1];
            var F = G[k[0]], E = k[1], A;
            for (A = 0; A < C.length && !(k = C[A], F = G[k[0]], E = k[1], C[A + 1] && f <= (F * E[E.length - 1] + G[C[A + 1][0]]) / 2); A++) ;
            F === G.year && f < 5 * F && (E = [1, 2, 5]);
            f = q(f / F, E, "year" === k[0] ? Math.max(H(f / F), 1) : 1);
            return {unitRange: F, count: f, unitName: k[0]}
        }
    });
    P(u, "parts/LogarithmicAxis.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.getMagnitude, q = k.normalizeTickInterval, G = k.pick;
        f = f.Axis;
        f.prototype.getLogTickPositions = function (f, k,
                                                    C, F) {
            var E = this.options, A = this.len, v = [];
            F || (this._minorAutoInterval = null);
            if (.5 <= f) f = Math.round(f), v = this.getLinearTickPositions(f, k, C); else if (.08 <= f) {
                A = Math.floor(k);
                var B, y;
                for (E = .3 < f ? [1, 2, 4] : .15 < f ? [1, 2, 4, 6, 8] : [1, 2, 3, 4, 5, 6, 7, 8, 9]; A < C + 1 && !y; A++) {
                    var p = E.length;
                    for (B = 0; B < p && !y; B++) {
                        var l = this.log2lin(this.lin2log(A) * E[B]);
                        l > k && (!F || n <= C) && "undefined" !== typeof n && v.push(n);
                        n > C && (y = !0);
                        var n = l
                    }
                }
            } else k = this.lin2log(k), C = this.lin2log(C), f = F ? this.getMinorTickInterval() : E.tickInterval, f = G("auto" === f ?
                null : f, this._minorAutoInterval, E.tickPixelInterval / (F ? 5 : 1) * (C - k) / ((F ? A / this.tickPositions.length : A) || 1)), f = q(f, null, H(f)), v = this.getLinearTickPositions(f, k, C).map(this.log2lin), F || (this._minorAutoInterval = f / 5);
            F || (this.tickInterval = f);
            return v
        };
        f.prototype.log2lin = function (f) {
            return Math.log(f) / Math.LN10
        };
        f.prototype.lin2log = function (f) {
            return Math.pow(10, f)
        }
    });
    P(u, "parts/PlotLineOrBand.js", [u["parts/Globals.js"], u["parts/Axis.js"], u["parts/Utilities.js"]], function (f, k, H) {
        var q = H.arrayMax, G = H.arrayMin,
            u = H.defined, L = H.destroyObjectProperties, C = H.erase, F = H.extend, E = H.merge, A = H.objectEach,
            v = H.pick, B = function () {
                function y(p, l) {
                    this.axis = p;
                    l && (this.options = l, this.id = l.id)
                }

                y.prototype.render = function () {
                    f.fireEvent(this, "render");
                    var p = this, l = p.axis, n = l.horiz, e = p.options, d = e.label, g = p.label, c = e.to, b = e.from,
                        a = e.value, w = u(b) && u(c), z = u(a), D = p.svgElem, r = !D, h = [], t = e.color,
                        I = v(e.zIndex, 0), J = e.events;
                    h = {"class": "highcharts-plot-" + (w ? "band " : "line ") + (e.className || "")};
                    var N = {}, x = l.chart.renderer, m = w ? "bands" :
                        "lines";
                    l.isLog && (b = l.log2lin(b), c = l.log2lin(c), a = l.log2lin(a));
                    l.chart.styledMode || (z ? (h.stroke = t || "#999999", h["stroke-width"] = v(e.width, 1), e.dashStyle && (h.dashstyle = e.dashStyle)) : w && (h.fill = t || "#e6ebf5", e.borderWidth && (h.stroke = e.borderColor, h["stroke-width"] = e.borderWidth)));
                    N.zIndex = I;
                    m += "-" + I;
                    (t = l.plotLinesAndBandsGroups[m]) || (l.plotLinesAndBandsGroups[m] = t = x.g("plot-" + m).attr(N).add());
                    r && (p.svgElem = D = x.path().attr(h).add(t));
                    if (z) h = l.getPlotLinePath({value: a, lineWidth: D.strokeWidth(), acrossPanes: e.acrossPanes});
                    else if (w) h = l.getPlotBandPath(b, c, e); else return;
                    (r || !D.d) && h && h.length ? (D.attr({d: h}), J && A(J, function (a, b) {
                        D.on(b, function (a) {
                            J[b].apply(p, [a])
                        })
                    })) : D && (h ? (D.show(!0), D.animate({d: h})) : D.d && (D.hide(), g && (p.label = g = g.destroy())));
                    d && (u(d.text) || u(d.formatter)) && h && h.length && 0 < l.width && 0 < l.height && !h.isFlat ? (d = E({
                        align: n && w && "center",
                        x: n ? !w && 4 : 10,
                        verticalAlign: !n && w && "middle",
                        y: n ? w ? 16 : 10 : w ? 6 : -4,
                        rotation: n && !w && 90
                    }, d), this.renderLabel(d, h, w, I)) : g && g.hide();
                    return p
                };
                y.prototype.renderLabel = function (p,
                                                    l, n, e) {
                    var d = this.label, g = this.axis.chart.renderer;
                    d || (d = {
                        align: p.textAlign || p.align,
                        rotation: p.rotation,
                        "class": "highcharts-plot-" + (n ? "band" : "line") + "-label " + (p.className || "")
                    }, d.zIndex = e, e = this.getLabelText(p), this.label = d = g.text(e, 0, 0, p.useHTML).attr(d).add(), this.axis.chart.styledMode || d.css(p.style));
                    g = l.xBounds || [l[1], l[4], n ? l[6] : l[1]];
                    l = l.yBounds || [l[2], l[5], n ? l[7] : l[2]];
                    n = G(g);
                    e = G(l);
                    d.align(p, !1, {x: n, y: e, width: q(g) - n, height: q(l) - e});
                    d.show(!0)
                };
                y.prototype.getLabelText = function (p) {
                    return u(p.formatter) ?
                        p.formatter.call(this) : p.text
                };
                y.prototype.destroy = function () {
                    C(this.axis.plotLinesAndBands, this);
                    delete this.axis;
                    L(this)
                };
                return y
            }();
        F(k.prototype, {
            getPlotBandPath: function (f, p) {
                var l = this.getPlotLinePath({value: p, force: !0, acrossPanes: this.options.acrossPanes}),
                    n = this.getPlotLinePath({value: f, force: !0, acrossPanes: this.options.acrossPanes}), e = [],
                    d = this.horiz, g = 1;
                f = f < this.min && p < this.min || f > this.max && p > this.max;
                if (n && l) {
                    if (f) {
                        var c = n.toString() === l.toString();
                        g = 0
                    }
                    for (f = 0; f < n.length; f += 6) d && l[f + 1] ===
                    n[f + 1] ? (l[f + 1] += g, l[f + 4] += g) : d || l[f + 2] !== n[f + 2] || (l[f + 2] += g, l[f + 5] += g), e.push("M", n[f + 1], n[f + 2], "L", n[f + 4], n[f + 5], l[f + 4], l[f + 5], l[f + 1], l[f + 2], "z"), e.isFlat = c
                }
                return e
            }, addPlotBand: function (f) {
                return this.addPlotBandOrLine(f, "plotBands")
            }, addPlotLine: function (f) {
                return this.addPlotBandOrLine(f, "plotLines")
            }, addPlotBandOrLine: function (f, p) {
                var l = (new B(this, f)).render(), n = this.userOptions;
                if (l) {
                    if (p) {
                        var e = n[p] || [];
                        e.push(f);
                        n[p] = e
                    }
                    this.plotLinesAndBands.push(l)
                }
                return l
            }, removePlotBandOrLine: function (f) {
                for (var p =
                    this.plotLinesAndBands, l = this.options, n = this.userOptions, e = p.length; e--;) p[e].id === f && p[e].destroy();
                [l.plotLines || [], n.plotLines || [], l.plotBands || [], n.plotBands || []].forEach(function (d) {
                    for (e = d.length; e--;) d[e].id === f && C(d, d[e])
                })
            }, removePlotBand: function (f) {
                this.removePlotBandOrLine(f)
            }, removePlotLine: function (f) {
                this.removePlotBandOrLine(f)
            }
        });
        f.PlotLineOrBand = B;
        return f.PlotLineOrBand
    });
    P(u, "parts/Tooltip.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.clamp, q = k.css,
            G = k.defined, u = k.discardElement, L = k.extend, C = k.format, F = k.isNumber, E = k.isString,
            A = k.merge, v = k.pick, B = k.splat, y = k.syncTimeout, p = k.timeUnits;
        "";
        var l = f.doc, n = function () {
            function e(d, e) {
                this.crosshairs = [];
                this.distance = 0;
                this.isHidden = !0;
                this.isSticky = !1;
                this.now = {};
                this.options = {};
                this.outside = !1;
                this.chart = d;
                this.init(d, e)
            }

            e.prototype.applyFilter = function () {
                var d = this.chart;
                d.renderer.definition({
                    tagName: "filter", id: "drop-shadow-" + d.index, opacity: .5, children: [{
                        tagName: "feGaussianBlur", "in": "SourceAlpha",
                        stdDeviation: 1
                    }, {tagName: "feOffset", dx: 1, dy: 1}, {
                        tagName: "feComponentTransfer",
                        children: [{tagName: "feFuncA", type: "linear", slope: .3}]
                    }, {
                        tagName: "feMerge",
                        children: [{tagName: "feMergeNode"}, {tagName: "feMergeNode", "in": "SourceGraphic"}]
                    }]
                });
                d.renderer.definition({
                    tagName: "style",
                    textContent: ".highcharts-tooltip-" + d.index + "{filter:url(#drop-shadow-" + d.index + ")}"
                })
            };
            e.prototype.bodyFormatter = function (d) {
                return d.map(function (d) {
                    var c = d.series.tooltipOptions;
                    return (c[(d.point.formatPrefix || "point") + "Formatter"] ||
                        d.point.tooltipFormatter).call(d.point, c[(d.point.formatPrefix || "point") + "Format"] || "")
                })
            };
            e.prototype.cleanSplit = function (d) {
                this.chart.series.forEach(function (e) {
                    var c = e && e.tt;
                    c && (!c.isActive || d ? e.tt = c.destroy() : c.isActive = !1)
                })
            };
            e.prototype.defaultFormatter = function (d) {
                var e = this.points || B(this);
                var c = [d.tooltipFooterHeaderFormatter(e[0])];
                c = c.concat(d.bodyFormatter(e));
                c.push(d.tooltipFooterHeaderFormatter(e[0], !0));
                return c
            };
            e.prototype.destroy = function () {
                this.label && (this.label = this.label.destroy());
                this.split && this.tt && (this.cleanSplit(this.chart, !0), this.tt = this.tt.destroy());
                this.renderer && (this.renderer = this.renderer.destroy(), u(this.container));
                k.clearTimeout(this.hideTimer);
                k.clearTimeout(this.tooltipTimeout)
            };
            e.prototype.getAnchor = function (d, e) {
                var c = this.chart, b = c.pointer, a = c.inverted, g = c.plotTop, z = c.plotLeft, l = 0, r = 0, h, t;
                d = B(d);
                this.followPointer && e ? ("undefined" === typeof e.chartX && (e = b.normalize(e)), d = [e.chartX - z, e.chartY - g]) : d[0].tooltipPos ? d = d[0].tooltipPos : (d.forEach(function (b) {
                    h =
                        b.series.yAxis;
                    t = b.series.xAxis;
                    l += b.plotX + (!a && t ? t.left - z : 0);
                    r += (b.plotLow ? (b.plotLow + b.plotHigh) / 2 : b.plotY) + (!a && h ? h.top - g : 0)
                }), l /= d.length, r /= d.length, d = [a ? c.plotWidth - r : l, this.shared && !a && 1 < d.length && e ? e.chartY - g : a ? c.plotHeight - l : r]);
                return d.map(Math.round)
            };
            e.prototype.getDateFormat = function (d, e, c, b) {
                var a = this.chart.time, g = a.dateFormat("%m-%d %H:%M:%S.%L", e),
                    z = {millisecond: 15, second: 12, minute: 9, hour: 6, day: 3}, l = "millisecond";
                for (r in p) {
                    if (d === p.week && +a.dateFormat("%w", e) === c && "00:00:00.000" ===
                        g.substr(6)) {
                        var r = "week";
                        break
                    }
                    if (p[r] > d) {
                        r = l;
                        break
                    }
                    if (z[r] && g.substr(z[r]) !== "01-01 00:00:00.000".substr(z[r])) break;
                    "week" !== r && (l = r)
                }
                if (r) var h = a.resolveDTLFormat(b[r]).main;
                return h
            };
            e.prototype.getLabel = function () {
                var d, e = this, c = this.chart.renderer, b = this.chart.styledMode, a = this.options,
                    w = "tooltip" + (G(a.className) ? " " + a.className : ""),
                    z = (null === (d = a.style) || void 0 === d ? void 0 : d.pointerEvents) || (!this.followPointer && a.stickOnContact ? "auto" : "none"),
                    l;
                d = function () {
                    e.inContact = !0
                };
                var r = function () {
                    var a =
                        e.chart.hoverSeries;
                    e.inContact = !1;
                    if (a && a.onMouseOut) a.onMouseOut()
                };
                if (!this.label) {
                    this.outside && (this.container = l = f.doc.createElement("div"), l.className = "highcharts-tooltip-container", q(l, {
                        position: "absolute",
                        top: "1px",
                        pointerEvents: z,
                        zIndex: 3
                    }), f.doc.body.appendChild(l), this.renderer = c = new f.Renderer(l, 0, 0, {}, void 0, void 0, c.styledMode));
                    this.split ? this.label = c.g(w) : (this.label = c.label("", 0, 0, a.shape || "callout", null, null, a.useHTML, null, w).attr({
                        padding: a.padding,
                        r: a.borderRadius
                    }), b || this.label.attr({
                        fill: a.backgroundColor,
                        "stroke-width": a.borderWidth
                    }).css(a.style).css({pointerEvents: z}).shadow(a.shadow));
                    b && (this.applyFilter(), this.label.addClass("highcharts-tooltip-" + this.chart.index));
                    if (e.outside && !e.split) {
                        var h = {x: this.label.xSetter, y: this.label.ySetter};
                        this.label.xSetter = function (a, b) {
                            h[b].call(this.label, e.distance);
                            l.style.left = a + "px"
                        };
                        this.label.ySetter = function (a, b) {
                            h[b].call(this.label, e.distance);
                            l.style.top = a + "px"
                        }
                    }
                    this.label.on("mouseenter", d).on("mouseleave", r).attr({zIndex: 8}).add()
                }
                return this.label
            };
            e.prototype.getPosition = function (d, e, c) {
                var b = this.chart, a = this.distance, g = {}, z = b.inverted && c.h || 0, n, r = this.outside,
                    h = r ? l.documentElement.clientWidth - 2 * a : b.chartWidth,
                    t = r ? Math.max(l.body.scrollHeight, l.documentElement.scrollHeight, l.body.offsetHeight, l.documentElement.offsetHeight, l.documentElement.clientHeight) : b.chartHeight,
                    I = b.pointer.getChartPosition(), f = b.containerScaling, p = function (a) {
                        return f ? a * f.scaleX : a
                    }, x = function (a) {
                        return f ? a * f.scaleY : a
                    }, m = function (m) {
                        var g = "x" === m;
                        return [m, g ? h : t, g ? d :
                            e].concat(r ? [g ? p(d) : x(e), g ? I.left - a + p(c.plotX + b.plotLeft) : I.top - a + x(c.plotY + b.plotTop), 0, g ? h : t] : [g ? d : e, g ? c.plotX + b.plotLeft : c.plotY + b.plotTop, g ? b.plotLeft : b.plotTop, g ? b.plotLeft + b.plotWidth : b.plotTop + b.plotHeight])
                    }, K = m("y"), O = m("x"), k = !this.followPointer && v(c.ttBelow, !b.inverted === !!c.negative),
                    A = function (b, c, h, d, m, e, w) {
                        var r = "y" === b ? x(a) : p(a), t = (h - d) / 2, l = d < m - a, n = m + a + d < c,
                            I = m - r - h + t;
                        m = m + r - t;
                        if (k && n) g[b] = m; else if (!k && l) g[b] = I; else if (l) g[b] = Math.min(w - d, 0 > I - z ? I : I - z); else if (n) g[b] = Math.max(e, m + z + h >
                        c ? m : m + z); else return !1
                    }, y = function (b, c, h, d, m) {
                        var e;
                        m < a || m > c - a ? e = !1 : g[b] = m < h / 2 ? 1 : m > c - d / 2 ? c - d - 2 : m - h / 2;
                        return e
                    }, B = function (a) {
                        var b = K;
                        K = O;
                        O = b;
                        n = a
                    }, q = function () {
                        !1 !== A.apply(0, K) ? !1 !== y.apply(0, O) || n || (B(!0), q()) : n ? g.x = g.y = 0 : (B(!0), q())
                    };
                (b.inverted || 1 < this.len) && B();
                q();
                return g
            };
            e.prototype.getXDateFormat = function (d, e, c) {
                e = e.dateTimeLabelFormats;
                var b = c && c.closestPointRange;
                return (b ? this.getDateFormat(b, d.x, c.options.startOfWeek, e) : e.day) || e.year
            };
            e.prototype.hide = function (d) {
                var e = this;
                k.clearTimeout(this.hideTimer);
                d = v(d, this.options.hideDelay, 500);
                this.isHidden || (this.hideTimer = y(function () {
                    e.getLabel()[d ? "fadeOut" : "hide"]();
                    e.isHidden = !0
                }, d))
            };
            e.prototype.init = function (d, e) {
                this.chart = d;
                this.options = e;
                this.crosshairs = [];
                this.now = {x: 0, y: 0};
                this.isHidden = !0;
                this.split = e.split && !d.inverted && !d.polar;
                this.shared = e.shared || this.split;
                this.outside = v(e.outside, !(!d.scrollablePixelsX && !d.scrollablePixelsY))
            };
            e.prototype.isStickyOnContact = function () {
                return !(this.followPointer || !this.options.stickOnContact || !this.inContact)
            };
            e.prototype.move = function (d, e, c, b) {
                var a = this, g = a.now,
                    z = !1 !== a.options.animation && !a.isHidden && (1 < Math.abs(d - g.x) || 1 < Math.abs(e - g.y)),
                    l = a.followPointer || 1 < a.len;
                L(g, {
                    x: z ? (2 * g.x + d) / 3 : d,
                    y: z ? (g.y + e) / 2 : e,
                    anchorX: l ? void 0 : z ? (2 * g.anchorX + c) / 3 : c,
                    anchorY: l ? void 0 : z ? (g.anchorY + b) / 2 : b
                });
                a.getLabel().attr(g);
                a.drawTracker();
                z && (k.clearTimeout(this.tooltipTimeout), this.tooltipTimeout = setTimeout(function () {
                    a && a.move(d, e, c, b)
                }, 32))
            };
            e.prototype.refresh = function (d, e) {
                var c = this.chart, b = this.options, a = d, g = {}, z = [],
                    l = b.formatter || this.defaultFormatter;
                g = this.shared;
                var r = c.styledMode;
                if (b.enabled) {
                    k.clearTimeout(this.hideTimer);
                    this.followPointer = B(a)[0].series.tooltipOptions.followPointer;
                    var h = this.getAnchor(a, e);
                    e = h[0];
                    var t = h[1];
                    !g || a.series && a.series.noSharedTooltip ? g = a.getLabelConfig() : (c.pointer.applyInactiveState(a), a.forEach(function (a) {
                        a.setState("hover");
                        z.push(a.getLabelConfig())
                    }), g = {x: a[0].category, y: a[0].y}, g.points = z, a = a[0]);
                    this.len = z.length;
                    c = l.call(g, this);
                    l = a.series;
                    this.distance = v(l.tooltipOptions.distance,
                        16);
                    !1 === c ? this.hide() : (this.split ? this.renderSplit(c, B(d)) : (d = this.getLabel(), b.style.width && !r || d.css({width: this.chart.spacingBox.width}), d.attr({text: c && c.join ? c.join("") : c}), d.removeClass(/highcharts-color-[\d]+/g).addClass("highcharts-color-" + v(a.colorIndex, l.colorIndex)), r || d.attr({stroke: b.borderColor || a.color || l.color || "#666666"}), this.updatePosition({
                        plotX: e,
                        plotY: t,
                        negative: a.negative,
                        ttBelow: a.ttBelow,
                        h: h[2] || 0
                    })), this.isHidden && this.label && this.label.attr({opacity: 1}).show(), this.isHidden =
                        !1);
                    f.fireEvent(this, "refresh")
                }
            };
            e.prototype.renderSplit = function (d, e) {
                function c(a, b, c, h, d) {
                    void 0 === d && (d = !0);
                    c ? (b = B ? 0 : F, a = H(a - h / 2, A.left, A.right - h)) : (b -= q, a = d ? a - h - K : a + K, a = H(a, d ? a : A.left, A.right));
                    return {x: a, y: b}
                }

                var b = this, a = b.chart, g = b.chart, z = g.plotHeight, l = g.plotLeft, r = g.plotTop, h = g.pointer,
                    t = g.renderer, n = g.scrollablePixelsY, p = void 0 === n ? 0 : n;
                n = g.scrollingContainer;
                n = void 0 === n ? {scrollLeft: 0, scrollTop: 0} : n;
                var N = n.scrollLeft, x = n.scrollTop, m = g.styledMode, K = b.distance, O = b.options,
                    k = b.options.positioner,
                    A = {left: N, right: N + g.chartWidth, top: x, bottom: x + g.chartHeight}, y = b.getLabel(),
                    B = !(!a.xAxis[0] || !a.xAxis[0].opposite), q = r + x, C = 0, F = z - p;
                E(d) && (d = [!1, d]);
                d = d.slice(0, e.length + 1).reduce(function (a, h, d) {
                    if (!1 !== h && "" !== h) {
                        d = e[d - 1] || {isHeader: !0, plotX: e[0].plotX, plotY: z, series: {}};
                        var g = d.isHeader, w = g ? b : d.series, n = w.tt, f = d.isHeader;
                        var I = d.series;
                        var D = "highcharts-color-" + v(d.colorIndex, I.colorIndex, "none");
                        n || (n = {
                            padding: O.padding,
                            r: O.borderRadius
                        }, m || (n.fill = O.backgroundColor, n["stroke-width"] = O.borderWidth),
                            n = t.label("", 0, 0, O[f ? "headerShape" : "shape"] || "callout", void 0, void 0, O.useHTML).addClass((f ? "highcharts-tooltip-header " : "") + "highcharts-tooltip-box " + D).attr(n).add(y));
                        n.isActive = !0;
                        n.attr({text: h});
                        m || n.css(O.style).shadow(O.shadow).attr({stroke: O.borderColor || d.color || I.color || "#333333"});
                        h = w.tt = n;
                        f = h.getBBox();
                        w = f.width + h.strokeWidth();
                        g && (C = f.height, F += C, B && (q -= C));
                        I = d.plotX;
                        I = void 0 === I ? 0 : I;
                        D = d.plotY;
                        D = void 0 === D ? 0 : D;
                        var J = d.series;
                        if (d.isHeader) {
                            I = l + I;
                            var N = r + z / 2
                        } else n = J.xAxis, J = J.yAxis,
                            I = n.pos + H(I, -K, n.len + K), J.pos + D >= x + r && J.pos + D <= x + r + z - p && (N = J.pos + D);
                        I = H(I, A.left - K, A.right + K);
                        "number" === typeof N ? (f = f.height + 1, D = k ? k.call(b, w, f, d) : c(I, N, g, w), a.push({
                            align: k ? 0 : void 0,
                            anchorX: I,
                            anchorY: N,
                            boxWidth: w,
                            point: d,
                            rank: v(D.rank, g ? 1 : 0),
                            size: f,
                            target: D.y,
                            tt: h,
                            x: D.x
                        })) : h.isActive = !1
                    }
                    return a
                }, []);
                !k && d.some(function (a) {
                    return a.x < A.left
                }) && (d = d.map(function (a) {
                    var b = c(a.anchorX, a.anchorY, a.point.isHeader, a.boxWidth, !1);
                    return L(a, {target: b.y, x: b.x})
                }));
                b.cleanSplit();
                f.distribute(d, F);
                d.forEach(function (a) {
                    var b =
                        a.pos;
                    a.tt.attr({
                        visibility: "undefined" === typeof b ? "hidden" : "inherit",
                        x: a.x,
                        y: b + q,
                        anchorX: a.anchorX,
                        anchorY: a.anchorY
                    })
                });
                d = b.container;
                a = b.renderer;
                b.outside && d && a && (g = y.getBBox(), a.setSize(g.width + g.x, g.height + g.y, !1), h = h.getChartPosition(), d.style.left = h.left + "px", d.style.top = h.top + "px")
            };
            e.prototype.drawTracker = function () {
                if (this.followPointer || !this.options.stickOnContact) this.tracker && this.tracker.destroy(); else {
                    var d = this.chart, e = this.label, c = d.hoverPoint;
                    if (e && c) {
                        var b = {x: 0, y: 0, width: 0, height: 0};
                        c = this.getAnchor(c);
                        var a = e.getBBox();
                        c[0] += d.plotLeft - e.translateX;
                        c[1] += d.plotTop - e.translateY;
                        b.x = Math.min(0, c[0]);
                        b.y = Math.min(0, c[1]);
                        b.width = 0 > c[0] ? Math.max(Math.abs(c[0]), a.width - c[0]) : Math.max(Math.abs(c[0]), a.width);
                        b.height = 0 > c[1] ? Math.max(Math.abs(c[1]), a.height - Math.abs(c[1])) : Math.max(Math.abs(c[1]), a.height);
                        this.tracker ? this.tracker.attr(b) : (this.tracker = e.renderer.rect(b).addClass("highcharts-tracker").add(e), d.styledMode || this.tracker.attr({fill: "rgba(0,0,0,0)"}))
                    }
                }
            };
            e.prototype.styledModeFormat =
                function (d) {
                    return d.replace('style="font-size: 10px"', 'class="highcharts-header"').replace(/style="color:{(point|series)\.color}"/g, 'class="highcharts-color-{$1.colorIndex}"')
                };
            e.prototype.tooltipFooterHeaderFormatter = function (d, e) {
                var c = e ? "footer" : "header", b = d.series, a = b.tooltipOptions, g = a.xDateFormat, z = b.xAxis,
                    l = z && "datetime" === z.options.type && F(d.key), r = a[c + "Format"];
                e = {isFooter: e, labelConfig: d};
                f.fireEvent(this, "headerFormatter", e, function (c) {
                    l && !g && (g = this.getXDateFormat(d, a, z));
                    l && g && (d.point &&
                        d.point.tooltipDateKeys || ["key"]).forEach(function (a) {
                        r = r.replace("{point." + a + "}", "{point." + a + ":" + g + "}")
                    });
                    b.chart.styledMode && (r = this.styledModeFormat(r));
                    c.text = C(r, {point: d, series: b}, this.chart)
                });
                return e.text
            };
            e.prototype.update = function (d) {
                this.destroy();
                A(!0, this.chart.options.tooltip.userOptions, d);
                this.init(this.chart, A(!0, this.options, d))
            };
            e.prototype.updatePosition = function (d) {
                var e = this.chart, c = e.pointer, b = this.getLabel(), a = d.plotX + e.plotLeft,
                    w = d.plotY + e.plotTop;
                c = c.getChartPosition();
                d = (this.options.positioner || this.getPosition).call(this, b.width, b.height, d);
                if (this.outside) {
                    var z = (this.options.borderWidth || 0) + 2 * this.distance;
                    this.renderer.setSize(b.width + z, b.height + z, !1);
                    if (e = e.containerScaling) q(this.container, {transform: "scale(" + e.scaleX + ", " + e.scaleY + ")"}), a *= e.scaleX, w *= e.scaleY;
                    a += c.left - d.x;
                    w += c.top - d.y
                }
                this.move(Math.round(d.x), Math.round(d.y || 0), a, w)
            };
            return e
        }();
        f.Tooltip = n;
        return f.Tooltip
    });
    P(u, "parts/Pointer.js", [u["parts/Globals.js"], u["parts/Utilities.js"], u["parts/Tooltip.js"],
        u["parts/Color.js"]], function (f, k, H, q) {
        var G = k.addEvent, u = k.attr, L = k.css, C = k.defined, F = k.extend, E = k.find, A = k.fireEvent,
            v = k.isNumber, B = k.isObject, y = k.objectEach, p = k.offset, l = k.pick, n = k.splat, e = q.parse,
            d = f.charts, g = f.noop;
        k = function () {
            function c(b, a) {
                this.lastValidTouch = {};
                this.pinchDown = [];
                this.runChartClick = !1;
                this.chart = b;
                this.hasDragged = !1;
                this.options = a;
                this.unbindContainerMouseLeave = function () {
                };
                this.init(b, a)
            }

            c.prototype.applyInactiveState = function (b) {
                var a = [], c;
                (b || []).forEach(function (b) {
                    c =
                        b.series;
                    a.push(c);
                    c.linkedParent && a.push(c.linkedParent);
                    c.linkedSeries && (a = a.concat(c.linkedSeries));
                    c.navigatorSeries && a.push(c.navigatorSeries)
                });
                this.chart.series.forEach(function (b) {
                    -1 === a.indexOf(b) ? b.setState("inactive", !0) : b.options.inactiveOtherPoints && b.setAllPointsToState("inactive")
                })
            };
            c.prototype.destroy = function () {
                var b = this;
                "undefined" !== typeof b.unDocMouseMove && b.unDocMouseMove();
                this.unbindContainerMouseLeave();
                f.chartCount || (f.unbindDocumentMouseUp && (f.unbindDocumentMouseUp = f.unbindDocumentMouseUp()),
                f.unbindDocumentTouchEnd && (f.unbindDocumentTouchEnd = f.unbindDocumentTouchEnd()));
                clearInterval(b.tooltipTimeout);
                y(b, function (a, c) {
                    b[c] = null
                })
            };
            c.prototype.drag = function (b) {
                var a = this.chart, c = a.options.chart, d = b.chartX, g = b.chartY, r = this.zoomHor,
                    h = this.zoomVert, t = a.plotLeft, l = a.plotTop, n = a.plotWidth, f = a.plotHeight,
                    x = this.selectionMarker, m = this.mouseDownX || 0, K = this.mouseDownY || 0,
                    p = B(c.panning) ? c.panning && c.panning.enabled : c.panning, v = c.panKey && b[c.panKey + "Key"];
                if (!x || !x.touch) if (d < t ? d = t : d > t + n && (d =
                    t + n), g < l ? g = l : g > l + f && (g = l + f), this.hasDragged = Math.sqrt(Math.pow(m - d, 2) + Math.pow(K - g, 2)), 10 < this.hasDragged) {
                    var k = a.isInsidePlot(m - t, K - l);
                    a.hasCartesianSeries && (this.zoomX || this.zoomY) && k && !v && !x && (this.selectionMarker = x = a.renderer.rect(t, l, r ? 1 : n, h ? 1 : f, 0).attr({
                        "class": "highcharts-selection-marker",
                        zIndex: 7
                    }).add(), a.styledMode || x.attr({fill: c.selectionMarkerFill || e("#335cad").setOpacity(.25).get()}));
                    x && r && (d -= m, x.attr({width: Math.abs(d), x: (0 < d ? 0 : d) + m}));
                    x && h && (d = g - K, x.attr({
                        height: Math.abs(d), y: (0 <
                        d ? 0 : d) + K
                    }));
                    k && !x && p && a.pan(b, c.panning)
                }
            };
            c.prototype.dragStart = function (b) {
                var a = this.chart;
                a.mouseIsDown = b.type;
                a.cancelClick = !1;
                a.mouseDownX = this.mouseDownX = b.chartX;
                a.mouseDownY = this.mouseDownY = b.chartY
            };
            c.prototype.drop = function (b) {
                var a = this, c = this.chart, d = this.hasPinched;
                if (this.selectionMarker) {
                    var e = {originalEvent: b, xAxis: [], yAxis: []}, g = this.selectionMarker,
                        h = g.attr ? g.attr("x") : g.x, t = g.attr ? g.attr("y") : g.y,
                        l = g.attr ? g.attr("width") : g.width, n = g.attr ? g.attr("height") : g.height, f;
                    if (this.hasDragged ||
                        d) c.axes.forEach(function (c) {
                        if (c.zoomEnabled && C(c.min) && (d || a[{xAxis: "zoomX", yAxis: "zoomY"}[c.coll]])) {
                            var m = c.horiz, g = "touchend" === b.type ? c.minPixelPadding : 0,
                                r = c.toValue((m ? h : t) + g);
                            m = c.toValue((m ? h + l : t + n) - g);
                            e[c.coll].push({axis: c, min: Math.min(r, m), max: Math.max(r, m)});
                            f = !0
                        }
                    }), f && A(c, "selection", e, function (a) {
                        c.zoom(F(a, d ? {animation: !1} : null))
                    });
                    v(c.index) && (this.selectionMarker = this.selectionMarker.destroy());
                    d && this.scaleGroups()
                }
                c && v(c.index) && (L(c.container, {cursor: c._cursor}), c.cancelClick =
                    10 < this.hasDragged, c.mouseIsDown = this.hasDragged = this.hasPinched = !1, this.pinchDown = [])
            };
            c.prototype.findNearestKDPoint = function (b, a, c) {
                var d = this.chart, e = d.hoverPoint;
                d = d.tooltip;
                if (e && d && d.isStickyOnContact()) return e;
                var g;
                b.forEach(function (b) {
                    var h = !(b.noSharedTooltip && a) && 0 > b.options.findNearestPointBy.indexOf("y");
                    b = b.searchPoint(c, h);
                    if ((h = B(b, !0)) && !(h = !B(g, !0))) {
                        h = g.distX - b.distX;
                        var d = g.dist - b.dist,
                            e = (b.series.group && b.series.group.zIndex) - (g.series.group && g.series.group.zIndex);
                        h = 0 < (0 !==
                        h && a ? h : 0 !== d ? d : 0 !== e ? e : g.series.index > b.series.index ? -1 : 1)
                    }
                    h && (g = b)
                });
                return g
            };
            c.prototype.getChartCoordinatesFromPoint = function (b, a) {
                var c = b.series, d = c.xAxis;
                c = c.yAxis;
                var e = l(b.clientX, b.plotX), g = b.shapeArgs;
                if (d && c) return a ? {
                    chartX: d.len + d.pos - e,
                    chartY: c.len + c.pos - b.plotY
                } : {chartX: e + d.pos, chartY: b.plotY + c.pos};
                if (g && g.x && g.y) return {chartX: g.x, chartY: g.y}
            };
            c.prototype.getChartPosition = function () {
                return this.chartPosition || (this.chartPosition = p(this.chart.container))
            };
            c.prototype.getCoordinates =
                function (b) {
                    var a = {xAxis: [], yAxis: []};
                    this.chart.axes.forEach(function (c) {
                        a[c.isXAxis ? "xAxis" : "yAxis"].push({
                            axis: c,
                            value: c.toValue(b[c.horiz ? "chartX" : "chartY"])
                        })
                    });
                    return a
                };
            c.prototype.getHoverData = function (b, a, c, d, e, g) {
                var h, r = [];
                d = !(!d || !b);
                var w = a && !a.stickyTracking,
                    z = {chartX: g ? g.chartX : void 0, chartY: g ? g.chartY : void 0, shared: e};
                A(this, "beforeGetHoverData", z);
                w = w ? [a] : c.filter(function (a) {
                    return z.filter ? z.filter(a) : a.visible && !(!e && a.directTouch) && l(a.options.enableMouseTracking, !0) && a.stickyTracking
                });
                a = (h = d || !g ? b : this.findNearestKDPoint(w, e, g)) && h.series;
                h && (e && !a.noSharedTooltip ? (w = c.filter(function (a) {
                    return z.filter ? z.filter(a) : a.visible && !(!e && a.directTouch) && l(a.options.enableMouseTracking, !0) && !a.noSharedTooltip
                }), w.forEach(function (a) {
                    var b = E(a.points, function (a) {
                        return a.x === h.x && !a.isNull
                    });
                    B(b) && (a.chart.isBoosting && (b = a.getPoint(b)), r.push(b))
                })) : r.push(h));
                z = {hoverPoint: h};
                A(this, "afterGetHoverData", z);
                return {hoverPoint: z.hoverPoint, hoverSeries: a, hoverPoints: r}
            };
            c.prototype.getPointFromEvent =
                function (b) {
                    b = b.target;
                    for (var a; b && !a;) a = b.point, b = b.parentNode;
                    return a
                };
            c.prototype.onTrackerMouseOut = function (b) {
                var a = this.chart.hoverSeries;
                b = b.relatedTarget || b.toElement;
                this.isDirectTouch = !1;
                if (!(!a || !b || a.stickyTracking || this.inClass(b, "highcharts-tooltip") || this.inClass(b, "highcharts-series-" + a.index) && this.inClass(b, "highcharts-tracker"))) a.onMouseOut()
            };
            c.prototype.inClass = function (b, a) {
                for (var c; b;) {
                    if (c = u(b, "class")) {
                        if (-1 !== c.indexOf(a)) return !0;
                        if (-1 !== c.indexOf("highcharts-container")) return !1
                    }
                    b =
                        b.parentNode
                }
            };
            c.prototype.init = function (b, a) {
                this.options = a;
                this.chart = b;
                this.runChartClick = a.chart.events && !!a.chart.events.click;
                this.pinchDown = [];
                this.lastValidTouch = {};
                H && (b.tooltip = new H(b, a.tooltip), this.followTouchMove = l(a.tooltip.followTouchMove, !0));
                this.setDOMEvents()
            };
            c.prototype.normalize = function (b, a) {
                var c = b.touches, d = c ? c.length ? c.item(0) : c.changedTouches[0] : b;
                a || (a = this.getChartPosition());
                c = d.pageX - a.left;
                a = d.pageY - a.top;
                if (d = this.chart.containerScaling) c /= d.scaleX, a /= d.scaleY;
                return F(b,
                    {chartX: Math.round(c), chartY: Math.round(a)})
            };
            c.prototype.onContainerClick = function (b) {
                var a = this.chart, c = a.hoverPoint, d = a.plotLeft, e = a.plotTop;
                b = this.normalize(b);
                a.cancelClick || (c && this.inClass(b.target, "highcharts-tracker") ? (A(c.series, "click", F(b, {point: c})), a.hoverPoint && c.firePointEvent("click", b)) : (F(b, this.getCoordinates(b)), a.isInsidePlot(b.chartX - d, b.chartY - e) && A(a, "click", b)))
            };
            c.prototype.onContainerMouseDown = function (b) {
                b = this.normalize(b);
                2 !== b.button && (this.zoomOption(b), b.preventDefault &&
                b.preventDefault(), this.dragStart(b))
            };
            c.prototype.onContainerMouseLeave = function (b) {
                var a = d[f.hoverChartIndex];
                a && (b.relatedTarget || b.toElement) && (a.pointer.reset(), a.pointer.chartPosition = void 0)
            };
            c.prototype.onContainerMouseMove = function (b) {
                var a = this.chart;
                C(f.hoverChartIndex) && d[f.hoverChartIndex] && d[f.hoverChartIndex].mouseIsDown || (f.hoverChartIndex = a.index);
                b = this.normalize(b);
                b.preventDefault || (b.returnValue = !1);
                "mousedown" === a.mouseIsDown && this.drag(b);
                a.openMenu || !this.inClass(b.target,
                    "highcharts-tracker") && !a.isInsidePlot(b.chartX - a.plotLeft, b.chartY - a.plotTop) || this.runPointActions(b)
            };
            c.prototype.onDocumentTouchEnd = function (b) {
                d[f.hoverChartIndex] && d[f.hoverChartIndex].pointer.drop(b)
            };
            c.prototype.onContainerTouchMove = function (b) {
                this.touch(b)
            };
            c.prototype.onContainerTouchStart = function (b) {
                this.zoomOption(b);
                this.touch(b, !0)
            };
            c.prototype.onDocumentMouseMove = function (b) {
                var a = this.chart, c = this.chartPosition, d = a.tooltip;
                b = this.normalize(b, c);
                !c || d && d.isStickyOnContact() || a.isInsidePlot(b.chartX -
                    a.plotLeft, b.chartY - a.plotTop) || this.inClass(b.target, "highcharts-tracker") || this.reset()
            };
            c.prototype.onDocumentMouseUp = function (b) {
                d[f.hoverChartIndex] && d[f.hoverChartIndex].pointer.drop(b)
            };
            c.prototype.pinch = function (b) {
                var a = this, c = a.chart, d = a.pinchDown, e = b.touches || [], r = e.length, h = a.lastValidTouch,
                    t = a.hasZoom, n = a.selectionMarker, f = {},
                    p = 1 === r && (a.inClass(b.target, "highcharts-tracker") && c.runTrackerClick || a.runChartClick),
                    x = {};
                1 < r && (a.initiated = !0);
                t && a.initiated && !p && b.preventDefault();
                [].map.call(e,
                    function (b) {
                        return a.normalize(b)
                    });
                "touchstart" === b.type ? ([].forEach.call(e, function (a, b) {
                    d[b] = {chartX: a.chartX, chartY: a.chartY}
                }), h.x = [d[0].chartX, d[1] && d[1].chartX], h.y = [d[0].chartY, d[1] && d[1].chartY], c.axes.forEach(function (a) {
                    if (a.zoomEnabled) {
                        var b = c.bounds[a.horiz ? "h" : "v"], d = a.minPixelPadding,
                            h = a.toPixels(Math.min(l(a.options.min, a.dataMin), a.dataMin)),
                            m = a.toPixels(Math.max(l(a.options.max, a.dataMax), a.dataMax)), e = Math.max(h, m);
                        b.min = Math.min(a.pos, Math.min(h, m) - d);
                        b.max = Math.max(a.pos + a.len,
                            e + d)
                    }
                }), a.res = !0) : a.followTouchMove && 1 === r ? this.runPointActions(a.normalize(b)) : d.length && (n || (a.selectionMarker = n = F({
                    destroy: g,
                    touch: !0
                }, c.plotBox)), a.pinchTranslate(d, e, f, n, x, h), a.hasPinched = t, a.scaleGroups(f, x), a.res && (a.res = !1, this.reset(!1, 0)))
            };
            c.prototype.pinchTranslate = function (b, a, c, d, e, g) {
                this.zoomHor && this.pinchTranslateDirection(!0, b, a, c, d, e, g);
                this.zoomVert && this.pinchTranslateDirection(!1, b, a, c, d, e, g)
            };
            c.prototype.pinchTranslateDirection = function (b, a, c, d, e, g, h, t) {
                var r = this.chart, w =
                        b ? "x" : "y", z = b ? "X" : "Y", l = "chart" + z, m = b ? "width" : "height",
                    n = r["plot" + (b ? "Left" : "Top")], f, p, D = t || 1, v = r.inverted, k = r.bounds[b ? "h" : "v"],
                    A = 1 === a.length, y = a[0][l], B = c[0][l], q = !A && a[1][l], S = !A && c[1][l];
                c = function () {
                    "number" === typeof S && 20 < Math.abs(y - q) && (D = t || Math.abs(B - S) / Math.abs(y - q));
                    p = (n - B) / D + y;
                    f = r["plot" + (b ? "Width" : "Height")] / D
                };
                c();
                a = p;
                if (a < k.min) {
                    a = k.min;
                    var E = !0
                } else a + f > k.max && (a = k.max - f, E = !0);
                E ? (B -= .8 * (B - h[w][0]), "number" === typeof S && (S -= .8 * (S - h[w][1])), c()) : h[w] = [B, S];
                v || (g[w] = p - n, g[m] = f);
                g = v ?
                    1 / D : D;
                e[m] = f;
                e[w] = a;
                d[v ? b ? "scaleY" : "scaleX" : "scale" + z] = D;
                d["translate" + z] = g * n + (B - g * y)
            };
            c.prototype.reset = function (b, a) {
                var c = this.chart, d = c.hoverSeries, e = c.hoverPoint, g = c.hoverPoints, h = c.tooltip,
                    t = h && h.shared ? g : e;
                b && t && n(t).forEach(function (a) {
                    a.series.isCartesian && "undefined" === typeof a.plotX && (b = !1)
                });
                if (b) h && t && n(t).length && (h.refresh(t), h.shared && g ? g.forEach(function (a) {
                    a.setState(a.state, !0);
                    a.series.isCartesian && (a.series.xAxis.crosshair && a.series.xAxis.drawCrosshair(null, a), a.series.yAxis.crosshair &&
                    a.series.yAxis.drawCrosshair(null, a))
                }) : e && (e.setState(e.state, !0), c.axes.forEach(function (a) {
                    a.crosshair && e.series[a.coll] === a && a.drawCrosshair(null, e)
                }))); else {
                    if (e) e.onMouseOut();
                    g && g.forEach(function (a) {
                        a.setState()
                    });
                    if (d) d.onMouseOut();
                    h && h.hide(a);
                    this.unDocMouseMove && (this.unDocMouseMove = this.unDocMouseMove());
                    c.axes.forEach(function (a) {
                        a.hideCrosshair()
                    });
                    this.hoverX = c.hoverPoints = c.hoverPoint = null
                }
            };
            c.prototype.runPointActions = function (b, a) {
                var c = this.chart, e = c.tooltip && c.tooltip.options.enabled ?
                    c.tooltip : void 0, g = e ? e.shared : !1, r = a || c.hoverPoint,
                    h = r && r.series || c.hoverSeries;
                h = this.getHoverData(r, h, c.series, (!b || "touchmove" !== b.type) && (!!a || h && h.directTouch && this.isDirectTouch), g, b);
                r = h.hoverPoint;
                var t = h.hoverPoints;
                a = (h = h.hoverSeries) && h.tooltipOptions.followPointer;
                g = g && h && !h.noSharedTooltip;
                if (r && (r !== c.hoverPoint || e && e.isHidden)) {
                    (c.hoverPoints || []).forEach(function (a) {
                        -1 === t.indexOf(a) && a.setState()
                    });
                    if (c.hoverSeries !== h) h.onMouseOver();
                    this.applyInactiveState(t);
                    (t || []).forEach(function (a) {
                        a.setState("hover")
                    });
                    c.hoverPoint && c.hoverPoint.firePointEvent("mouseOut");
                    if (!r.series) return;
                    r.firePointEvent("mouseOver");
                    c.hoverPoints = t;
                    c.hoverPoint = r;
                    e && e.refresh(g ? t : r, b)
                } else a && e && !e.isHidden && (r = e.getAnchor([{}], b), e.updatePosition({
                    plotX: r[0],
                    plotY: r[1]
                }));
                this.unDocMouseMove || (this.unDocMouseMove = G(c.container.ownerDocument, "mousemove", function (a) {
                    var b = d[f.hoverChartIndex];
                    if (b) b.pointer.onDocumentMouseMove(a)
                }));
                c.axes.forEach(function (a) {
                    var c = l(a.crosshair.snap, !0), d = c ? E(t, function (b) {
                        return b.series[a.coll] ===
                            a
                    }) : void 0;
                    d || !c ? a.drawCrosshair(b, d) : a.hideCrosshair()
                })
            };
            c.prototype.scaleGroups = function (b, a) {
                var c = this.chart, d;
                c.series.forEach(function (e) {
                    d = b || e.getPlotBox();
                    e.xAxis && e.xAxis.zoomEnabled && e.group && (e.group.attr(d), e.markerGroup && (e.markerGroup.attr(d), e.markerGroup.clip(a ? c.clipRect : null)), e.dataLabelsGroup && e.dataLabelsGroup.attr(d))
                });
                c.clipRect.attr(a || c.clipBox)
            };
            c.prototype.setDOMEvents = function () {
                var b = this, a = b.chart.container, c = a.ownerDocument;
                a.onmousedown = function (a) {
                    b.onContainerMouseDown(a)
                };
                a.onmousemove = function (a) {
                    b.onContainerMouseMove(a)
                };
                a.onclick = function (a) {
                    b.onContainerClick(a)
                };
                this.unbindContainerMouseLeave = G(a, "mouseleave", b.onContainerMouseLeave);
                f.unbindDocumentMouseUp || (f.unbindDocumentMouseUp = G(c, "mouseup", b.onDocumentMouseUp));
                f.hasTouch && (G(a, "touchstart", function (a) {
                    b.onContainerTouchStart(a)
                }), G(a, "touchmove", function (a) {
                    b.onContainerTouchMove(a)
                }), f.unbindDocumentTouchEnd || (f.unbindDocumentTouchEnd = G(c, "touchend", b.onDocumentTouchEnd)))
            };
            c.prototype.touch = function (b,
                                          a) {
                var c = this.chart, d;
                if (c.index !== f.hoverChartIndex) this.onContainerMouseLeave({relatedTarget: !0});
                f.hoverChartIndex = c.index;
                if (1 === b.touches.length) if (b = this.normalize(b), (d = c.isInsidePlot(b.chartX - c.plotLeft, b.chartY - c.plotTop)) && !c.openMenu) {
                    a && this.runPointActions(b);
                    if ("touchmove" === b.type) {
                        a = this.pinchDown;
                        var e = a[0] ? 4 <= Math.sqrt(Math.pow(a[0].chartX - b.chartX, 2) + Math.pow(a[0].chartY - b.chartY, 2)) : !1
                    }
                    l(e, !0) && this.pinch(b)
                } else a && this.reset(); else 2 === b.touches.length && this.pinch(b)
            };
            c.prototype.zoomOption =
                function (b) {
                    var a = this.chart, c = a.options.chart, d = c.zoomType || "";
                    a = a.inverted;
                    /touch/.test(b.type) && (d = l(c.pinchType, d));
                    this.zoomX = b = /x/.test(d);
                    this.zoomY = d = /y/.test(d);
                    this.zoomHor = b && !a || d && a;
                    this.zoomVert = d && !a || b && a;
                    this.hasZoom = b || d
                };
            return c
        }();
        f.Pointer = k;
        return f.Pointer
    });
    P(u, "parts/MSPointer.js", [u["parts/Globals.js"], u["parts/Pointer.js"], u["parts/Utilities.js"]], function (f, k, H) {
        function q() {
            var l = [];
            l.item = function (l) {
                return this[l]
            };
            F(y, function (n) {
                l.push({
                    pageX: n.pageX, pageY: n.pageY,
                    target: n.target
                })
            });
            return l
        }

        function G(l, n, e, d) {
            "touch" !== l.pointerType && l.pointerType !== l.MSPOINTER_TYPE_TOUCH || !A[f.hoverChartIndex] || (d(l), d = A[f.hoverChartIndex].pointer, d[n]({
                type: e,
                target: l.currentTarget,
                preventDefault: B,
                touches: q()
            }))
        }

        var u = this && this.__extends || function () {
                var l = function (n, e) {
                    l = Object.setPrototypeOf || {__proto__: []} instanceof Array && function (d, e) {
                        d.__proto__ = e
                    } || function (d, e) {
                        for (var c in e) e.hasOwnProperty(c) && (d[c] = e[c])
                    };
                    return l(n, e)
                };
                return function (n, e) {
                    function d() {
                        this.constructor =
                            n
                    }

                    l(n, e);
                    n.prototype = null === e ? Object.create(e) : (d.prototype = e.prototype, new d)
                }
            }(), L = H.addEvent, C = H.css, F = H.objectEach, E = H.removeEvent, A = f.charts, v = f.doc, B = f.noop,
            y = {}, p = !!f.win.PointerEvent;
        return function (l) {
            function n() {
                return null !== l && l.apply(this, arguments) || this
            }

            u(n, l);
            n.prototype.batchMSEvents = function (e) {
                e(this.chart.container, p ? "pointerdown" : "MSPointerDown", this.onContainerPointerDown);
                e(this.chart.container, p ? "pointermove" : "MSPointerMove", this.onContainerPointerMove);
                e(v, p ? "pointerup" :
                    "MSPointerUp", this.onDocumentPointerUp)
            };
            n.prototype.destroy = function () {
                this.batchMSEvents(E);
                l.prototype.destroy.call(this)
            };
            n.prototype.init = function (e, d) {
                l.prototype.init.call(this, e, d);
                this.hasZoom && C(e.container, {"-ms-touch-action": "none", "touch-action": "none"})
            };
            n.prototype.onContainerPointerDown = function (e) {
                G(e, "onContainerTouchStart", "touchstart", function (d) {
                    y[d.pointerId] = {pageX: d.pageX, pageY: d.pageY, target: d.currentTarget}
                })
            };
            n.prototype.onContainerPointerMove = function (e) {
                G(e, "onContainerTouchMove",
                    "touchmove", function (d) {
                        y[d.pointerId] = {pageX: d.pageX, pageY: d.pageY};
                        y[d.pointerId].target || (y[d.pointerId].target = d.currentTarget)
                    })
            };
            n.prototype.onDocumentPointerUp = function (e) {
                G(e, "onDocumentTouchEnd", "touchend", function (d) {
                    delete y[d.pointerId]
                })
            };
            n.prototype.setDOMEvents = function () {
                l.prototype.setDOMEvents.call(this);
                (this.hasZoom || this.followTouchMove) && this.batchMSEvents(L)
            };
            return n
        }(k)
    });
    P(u, "parts/Legend.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var H = k.addEvent,
            q = k.css, G = k.defined, u = k.discardElement, L = k.find, C = k.fireEvent, F = k.format, E = k.isNumber,
            A = k.merge, v = k.pick, B = k.relativeLength, y = k.setAnimation, p = k.stableSort, l = k.syncTimeout;
        k = k.wrap;
        var n = f.isFirefox, e = f.marginNames, d = f.win, g = function () {
            function c(b, a) {
                this.allItems = [];
                this.contentGroup = this.box = void 0;
                this.display = !1;
                this.group = void 0;
                this.offsetWidth = this.maxLegendWidth = this.maxItemWidth = this.legendWidth = this.legendHeight = this.lastLineHeight = this.lastItemY = this.itemY = this.itemX = this.itemMarginTop =
                    this.itemMarginBottom = this.itemHeight = this.initialItemY = 0;
                this.options = {};
                this.padding = 0;
                this.pages = [];
                this.proximate = !1;
                this.scrollGroup = void 0;
                this.widthOption = this.totalItemWidth = this.titleHeight = this.symbolWidth = this.symbolHeight = 0;
                this.chart = b;
                this.init(b, a)
            }

            c.prototype.init = function (b, a) {
                this.chart = b;
                this.setOptions(a);
                a.enabled && (this.render(), H(this.chart, "endResize", function () {
                    this.legend.positionCheckboxes()
                }), this.proximate ? this.unchartrender = H(this.chart, "render", function () {
                    this.legend.proximatePositions();
                    this.legend.positionItems()
                }) : this.unchartrender && this.unchartrender())
            };
            c.prototype.setOptions = function (b) {
                var a = v(b.padding, 8);
                this.options = b;
                this.chart.styledMode || (this.itemStyle = b.itemStyle, this.itemHiddenStyle = A(this.itemStyle, b.itemHiddenStyle));
                this.itemMarginTop = b.itemMarginTop || 0;
                this.itemMarginBottom = b.itemMarginBottom || 0;
                this.padding = a;
                this.initialItemY = a - 5;
                this.symbolWidth = v(b.symbolWidth, 16);
                this.pages = [];
                this.proximate = "proximate" === b.layout && !this.chart.inverted;
                this.baseline = void 0
            };
            c.prototype.update = function (b, a) {
                var c = this.chart;
                this.setOptions(A(!0, this.options, b));
                this.destroy();
                c.isDirtyLegend = c.isDirtyBox = !0;
                v(a, !0) && c.redraw();
                C(this, "afterUpdate")
            };
            c.prototype.colorizeItem = function (b, a) {
                b.legendGroup[a ? "removeClass" : "addClass"]("highcharts-legend-item-hidden");
                if (!this.chart.styledMode) {
                    var c = this.options, d = b.legendItem, e = b.legendLine, g = b.legendSymbol,
                        h = this.itemHiddenStyle.color;
                    c = a ? c.itemStyle.color : h;
                    var t = a ? b.color || h : h, l = b.options && b.options.marker, n = {fill: t};
                    d &&
                    d.css({fill: c, color: c});
                    e && e.attr({stroke: t});
                    g && (l && g.isMarker && (n = b.pointAttribs(), a || (n.stroke = n.fill = h)), g.attr(n))
                }
                C(this, "afterColorizeItem", {item: b, visible: a})
            };
            c.prototype.positionItems = function () {
                this.allItems.forEach(this.positionItem, this);
                this.chart.isResizing || this.positionCheckboxes()
            };
            c.prototype.positionItem = function (b) {
                var a = this.options, c = a.symbolPadding;
                a = !a.rtl;
                var d = b._legendItemPos, e = d[0];
                d = d[1];
                var g = b.checkbox;
                if ((b = b.legendGroup) && b.element) b[G(b.translateY) ? "animate" : "attr"]({
                    translateX: a ?
                        e : this.legendWidth - e - 2 * c - 4, translateY: d
                });
                g && (g.x = e, g.y = d)
            };
            c.prototype.destroyItem = function (b) {
                var a = b.checkbox;
                ["legendItem", "legendLine", "legendSymbol", "legendGroup"].forEach(function (a) {
                    b[a] && (b[a] = b[a].destroy())
                });
                a && u(b.checkbox)
            };
            c.prototype.destroy = function () {
                function b(a) {
                    this[a] && (this[a] = this[a].destroy())
                }

                this.getAllItems().forEach(function (a) {
                    ["legendItem", "legendGroup"].forEach(b, a)
                });
                "clipRect up down pager nav box title group".split(" ").forEach(b, this);
                this.display = null
            };
            c.prototype.positionCheckboxes =
                function () {
                    var b = this.group && this.group.alignAttr, a = this.clipHeight || this.legendHeight,
                        c = this.titleHeight;
                    if (b) {
                        var d = b.translateY;
                        this.allItems.forEach(function (e) {
                            var g = e.checkbox;
                            if (g) {
                                var h = d + c + g.y + (this.scrollOffset || 0) + 3;
                                q(g, {
                                    left: b.translateX + e.checkboxOffset + g.x - 20 + "px",
                                    top: h + "px",
                                    display: this.proximate || h > d - 6 && h < d + a - 6 ? "" : "none"
                                })
                            }
                        }, this)
                    }
                };
            c.prototype.renderTitle = function () {
                var b = this.options, a = this.padding, c = b.title, d = 0;
                c.text && (this.title || (this.title = this.chart.renderer.label(c.text, a -
                    3, a - 4, null, null, null, b.useHTML, null, "legend-title").attr({zIndex: 1}), this.chart.styledMode || this.title.css(c.style), this.title.add(this.group)), c.width || this.title.css({width: this.maxLegendWidth + "px"}), b = this.title.getBBox(), d = b.height, this.offsetWidth = b.width, this.contentGroup.attr({translateY: d}));
                this.titleHeight = d
            };
            c.prototype.setText = function (b) {
                var a = this.options;
                b.legendItem.attr({text: a.labelFormat ? F(a.labelFormat, b, this.chart) : a.labelFormatter.call(b)})
            };
            c.prototype.renderItem = function (b) {
                var a =
                        this.chart, c = a.renderer, d = this.options, e = this.symbolWidth, g = d.symbolPadding,
                    h = this.itemStyle, t = this.itemHiddenStyle,
                    l = "horizontal" === d.layout ? v(d.itemDistance, 20) : 0, n = !d.rtl, f = b.legendItem,
                    x = !b.series, m = !x && b.series.drawLegendSymbol ? b.series : b, K = m.options;
                K = this.createCheckboxForItem && K && K.showCheckbox;
                l = e + g + l + (K ? 20 : 0);
                var p = d.useHTML, k = b.options.className;
                f || (b.legendGroup = c.g("legend-item").addClass("highcharts-" + m.type + "-series highcharts-color-" + b.colorIndex + (k ? " " + k : "") + (x ? " highcharts-series-" +
                    b.index : "")).attr({zIndex: 1}).add(this.scrollGroup), b.legendItem = f = c.text("", n ? e + g : -g, this.baseline || 0, p), a.styledMode || f.css(A(b.visible ? h : t)), f.attr({
                    align: n ? "left" : "right",
                    zIndex: 2
                }).add(b.legendGroup), this.baseline || (this.fontMetrics = c.fontMetrics(a.styledMode ? 12 : h.fontSize, f), this.baseline = this.fontMetrics.f + 3 + this.itemMarginTop, f.attr("y", this.baseline)), this.symbolHeight = d.symbolHeight || this.fontMetrics.f, m.drawLegendSymbol(this, b), this.setItemEvents && this.setItemEvents(b, f, p));
                K && !b.checkbox &&
                this.createCheckboxForItem && this.createCheckboxForItem(b);
                this.colorizeItem(b, b.visible);
                !a.styledMode && h.width || f.css({width: (d.itemWidth || this.widthOption || a.spacingBox.width) - l});
                this.setText(b);
                a = f.getBBox();
                b.itemWidth = b.checkboxOffset = d.itemWidth || b.legendItemWidth || a.width + l;
                this.maxItemWidth = Math.max(this.maxItemWidth, b.itemWidth);
                this.totalItemWidth += b.itemWidth;
                this.itemHeight = b.itemHeight = Math.round(b.legendItemHeight || a.height || this.symbolHeight)
            };
            c.prototype.layoutItem = function (b) {
                var a =
                        this.options, c = this.padding, d = "horizontal" === a.layout, e = b.itemHeight,
                    g = this.itemMarginBottom, h = this.itemMarginTop, t = d ? v(a.itemDistance, 20) : 0,
                    l = this.maxLegendWidth;
                a = a.alignColumns && this.totalItemWidth > l ? this.maxItemWidth : b.itemWidth;
                d && this.itemX - c + a > l && (this.itemX = c, this.lastLineHeight && (this.itemY += h + this.lastLineHeight + g), this.lastLineHeight = 0);
                this.lastItemY = h + this.itemY + g;
                this.lastLineHeight = Math.max(e, this.lastLineHeight);
                b._legendItemPos = [this.itemX, this.itemY];
                d ? this.itemX += a : (this.itemY +=
                    h + e + g, this.lastLineHeight = e);
                this.offsetWidth = this.widthOption || Math.max((d ? this.itemX - c - (b.checkbox ? 0 : t) : a) + c, this.offsetWidth)
            };
            c.prototype.getAllItems = function () {
                var b = [];
                this.chart.series.forEach(function (a) {
                    var c = a && a.options;
                    a && v(c.showInLegend, G(c.linkedTo) ? !1 : void 0, !0) && (b = b.concat(a.legendItems || ("point" === c.legendType ? a.data : a)))
                });
                C(this, "afterGetAllItems", {allItems: b});
                return b
            };
            c.prototype.getAlignment = function () {
                var b = this.options;
                return this.proximate ? b.align.charAt(0) + "tv" : b.floating ?
                    "" : b.align.charAt(0) + b.verticalAlign.charAt(0) + b.layout.charAt(0)
            };
            c.prototype.adjustMargins = function (b, a) {
                var c = this.chart, d = this.options, g = this.getAlignment();
                g && [/(lth|ct|rth)/, /(rtv|rm|rbv)/, /(rbh|cb|lbh)/, /(lbv|lm|ltv)/].forEach(function (r, h) {
                    r.test(g) && !G(b[h]) && (c[e[h]] = Math.max(c[e[h]], c.legend[(h + 1) % 2 ? "legendHeight" : "legendWidth"] + [1, -1, -1, 1][h] * d[h % 2 ? "x" : "y"] + v(d.margin, 12) + a[h] + (c.titleOffset[h] || 0)))
                })
            };
            c.prototype.proximatePositions = function () {
                var b = this.chart, a = [], c = "left" === this.options.align;
                this.allItems.forEach(function (d) {
                    var e = c;
                    if (d.yAxis && d.points) {
                        d.xAxis.options.reversed && (e = !e);
                        var g = L(e ? d.points : d.points.slice(0).reverse(), function (a) {
                            return E(a.plotY)
                        });
                        e = this.itemMarginTop + d.legendItem.getBBox().height + this.itemMarginBottom;
                        var h = d.yAxis.top - b.plotTop;
                        d.visible ? (g = g ? g.plotY : d.yAxis.height, g += h - .3 * e) : g = h + d.yAxis.height;
                        a.push({target: g, size: e, item: d})
                    }
                }, this);
                f.distribute(a, b.plotHeight);
                a.forEach(function (a) {
                    a.item._legendItemPos[1] = b.plotTop - b.spacing[0] + a.pos
                })
            };
            c.prototype.render =
                function () {
                    var b = this.chart, a = b.renderer, c = this.group, d, e = this.box, g = this.options,
                        h = this.padding;
                    this.itemX = h;
                    this.itemY = this.initialItemY;
                    this.lastItemY = this.offsetWidth = 0;
                    this.widthOption = B(g.width, b.spacingBox.width - h);
                    var t = b.spacingBox.width - 2 * h - g.x;
                    -1 < ["rm", "lm"].indexOf(this.getAlignment().substring(0, 2)) && (t /= 2);
                    this.maxLegendWidth = this.widthOption || t;
                    c || (this.group = c = a.g("legend").attr({zIndex: 7}).add(), this.contentGroup = a.g().attr({zIndex: 1}).add(c), this.scrollGroup = a.g().add(this.contentGroup));
                    this.renderTitle();
                    t = this.getAllItems();
                    p(t, function (a, b) {
                        return (a.options && a.options.legendIndex || 0) - (b.options && b.options.legendIndex || 0)
                    });
                    g.reversed && t.reverse();
                    this.allItems = t;
                    this.display = d = !!t.length;
                    this.itemHeight = this.totalItemWidth = this.maxItemWidth = this.lastLineHeight = 0;
                    t.forEach(this.renderItem, this);
                    t.forEach(this.layoutItem, this);
                    t = (this.widthOption || this.offsetWidth) + h;
                    var l = this.lastItemY + this.lastLineHeight + this.titleHeight;
                    l = this.handleOverflow(l);
                    l += h;
                    e || (this.box = e = a.rect().addClass("highcharts-legend-box").attr({r: g.borderRadius}).add(c),
                        e.isNew = !0);
                    b.styledMode || e.attr({
                        stroke: g.borderColor,
                        "stroke-width": g.borderWidth || 0,
                        fill: g.backgroundColor || "none"
                    }).shadow(g.shadow);
                    0 < t && 0 < l && (e[e.isNew ? "attr" : "animate"](e.crisp.call({}, {
                        x: 0,
                        y: 0,
                        width: t,
                        height: l
                    }, e.strokeWidth())), e.isNew = !1);
                    e[d ? "show" : "hide"]();
                    b.styledMode && "none" === c.getStyle("display") && (t = l = 0);
                    this.legendWidth = t;
                    this.legendHeight = l;
                    d && (a = b.spacingBox, e = a.y, /(lth|ct|rth)/.test(this.getAlignment()) && 0 < b.titleOffset[0] ? e += b.titleOffset[0] : /(lbh|cb|rbh)/.test(this.getAlignment()) &&
                        0 < b.titleOffset[2] && (e -= b.titleOffset[2]), e !== a.y && (a = A(a, {y: e})), c.align(A(g, {
                        width: t,
                        height: l,
                        verticalAlign: this.proximate ? "top" : g.verticalAlign
                    }), !0, a));
                    this.proximate || this.positionItems();
                    C(this, "afterRender")
                };
            c.prototype.handleOverflow = function (b) {
                var a = this, c = this.chart, d = c.renderer, e = this.options, g = e.y, h = this.padding;
                g = c.spacingBox.height + ("top" === e.verticalAlign ? -g : g) - h;
                var t = e.maxHeight, l, n = this.clipRect, f = e.navigation, x = v(f.animation, !0),
                    m = f.arrowSize || 12, K = this.nav, p = this.pages, k, A = this.allItems,
                    y = function (b) {
                        "number" === typeof b ? n.attr({height: b}) : n && (a.clipRect = n.destroy(), a.contentGroup.clip());
                        a.contentGroup.div && (a.contentGroup.div.style.clip = b ? "rect(" + h + "px,9999px," + (h + b) + "px,0)" : "auto")
                    }, B = function (b) {
                        a[b] = d.circle(0, 0, 1.3 * m).translate(m / 2, m / 2).add(K);
                        c.styledMode || a[b].attr("fill", "rgba(0,0,0,0.0001)");
                        return a[b]
                    };
                "horizontal" !== e.layout || "middle" === e.verticalAlign || e.floating || (g /= 2);
                t && (g = Math.min(g, t));
                p.length = 0;
                b > g && !1 !== f.enabled ? (this.clipHeight = l = Math.max(g - 20 - this.titleHeight -
                    h, 0), this.currentPage = v(this.currentPage, 1), this.fullHeight = b, A.forEach(function (a, b) {
                    var c = a._legendItemPos[1], d = Math.round(a.legendItem.getBBox().height), h = p.length;
                    if (!h || c - p[h - 1] > l && (k || c) !== p[h - 1]) p.push(k || c), h++;
                    a.pageIx = h - 1;
                    k && (A[b - 1].pageIx = h - 1);
                    b === A.length - 1 && c + d - p[h - 1] > l && c !== k && (p.push(c), a.pageIx = h);
                    c !== k && (k = c)
                }), n || (n = a.clipRect = d.clipRect(0, h, 9999, 0), a.contentGroup.clip(n)), y(l), K || (this.nav = K = d.g().attr({zIndex: 1}).add(this.group), this.up = d.symbol("triangle", 0, 0, m, m).add(K), B("upTracker").on("click",
                    function () {
                        a.scroll(-1, x)
                    }), this.pager = d.text("", 15, 10).addClass("highcharts-legend-navigation"), c.styledMode || this.pager.css(f.style), this.pager.add(K), this.down = d.symbol("triangle-down", 0, 0, m, m).add(K), B("downTracker").on("click", function () {
                    a.scroll(1, x)
                })), a.scroll(0), b = g) : K && (y(), this.nav = K.destroy(), this.scrollGroup.attr({translateY: 1}), this.clipHeight = 0);
                return b
            };
            c.prototype.scroll = function (b, a) {
                var c = this, d = this.chart, e = this.pages, g = e.length, h = this.currentPage + b;
                b = this.clipHeight;
                var t = this.options.navigation,
                    n = this.pager, p = this.padding;
                h > g && (h = g);
                0 < h && ("undefined" !== typeof a && y(a, d), this.nav.attr({
                    translateX: p,
                    translateY: b + this.padding + 7 + this.titleHeight,
                    visibility: "visible"
                }), [this.up, this.upTracker].forEach(function (a) {
                    a.attr({"class": 1 === h ? "highcharts-legend-nav-inactive" : "highcharts-legend-nav-active"})
                }), n.attr({text: h + "/" + g}), [this.down, this.downTracker].forEach(function (a) {
                    a.attr({
                        x: 18 + this.pager.getBBox().width,
                        "class": h === g ? "highcharts-legend-nav-inactive" : "highcharts-legend-nav-active"
                    })
                }, this),
                d.styledMode || (this.up.attr({fill: 1 === h ? t.inactiveColor : t.activeColor}), this.upTracker.css({cursor: 1 === h ? "default" : "pointer"}), this.down.attr({fill: h === g ? t.inactiveColor : t.activeColor}), this.downTracker.css({cursor: h === g ? "default" : "pointer"})), this.scrollOffset = -e[h - 1] + this.initialItemY, this.scrollGroup.animate({translateY: this.scrollOffset}), this.currentPage = h, this.positionCheckboxes(), a = f.animObject(v(a, d.renderer.globalAnimation, !0)), l(function () {
                    C(c, "afterScroll", {currentPage: h})
                }, a.duration ||
                    0))
            };
            return c
        }();
        (/Trident\/7\.0/.test(d.navigator && d.navigator.userAgent) || n) && k(g.prototype, "positionItem", function (c, b) {
            var a = this, d = function () {
                b._legendItemPos && c.call(a, b)
            };
            d();
            a.bubbleLegend || setTimeout(d)
        });
        f.Legend = g;
        return f.Legend
    });
    P(u, "parts/Chart.js", [u["parts/Globals.js"], u["parts/Legend.js"], u["parts/MSPointer.js"], u["parts/Pointer.js"], u["parts/Time.js"], u["parts/Utilities.js"]], function (f, k, H, q, G, u) {
        var L = u.addEvent, C = u.animate, F = u.animObject, E = u.attr, A = u.createElement, v = u.css, B =
                u.defined, y = u.discardElement, p = u.erase, l = u.error, n = u.extend, e = u.find, d = u.fireEvent,
            g = u.getStyle, c = u.isArray, b = u.isFunction, a = u.isNumber, w = u.isObject, z = u.isString,
            D = u.merge, r = u.numberFormat, h = u.objectEach, t = u.pick, I = u.pInt, J = u.relativeLength,
            N = u.removeEvent, x = u.setAnimation, m = u.splat, K = u.syncTimeout, O = u.uniqueKey, V = f.doc,
            U = f.Axis, X = f.defaultOptions, Q = f.charts, R = f.marginNames, T = f.seriesTypes, M = f.win,
            Z = f.Chart = function () {
                this.getArgs.apply(this, arguments)
            };
        f.chart = function (a, b, c) {
            return new Z(a, b, c)
        };
        n(Z.prototype, {
            callbacks: [], getArgs: function () {
                var a = [].slice.call(arguments);
                if (z(a[0]) || a[0].nodeName) this.renderTo = a.shift();
                this.init(a[0], a[1])
            }, init: function (a, c) {
                var e, m = a.series, g = a.plotOptions || {};
                d(this, "init", {args: arguments}, function () {
                    a.series = null;
                    e = D(X, a);
                    h(e.plotOptions, function (a, b) {
                        w(a) && (a.tooltip = g[b] && D(g[b].tooltip) || void 0)
                    });
                    e.tooltip.userOptions = a.chart && a.chart.forExport && a.tooltip.userOptions || a.tooltip;
                    e.series = a.series = m;
                    this.userOptions = a;
                    var t = e.chart, l = t.events;
                    this.margin =
                        [];
                    this.spacing = [];
                    this.bounds = {h: {}, v: {}};
                    this.labelCollectors = [];
                    this.callback = c;
                    this.isResizing = 0;
                    this.options = e;
                    this.axes = [];
                    this.series = [];
                    this.time = a.time && Object.keys(a.time).length ? new G(a.time) : f.time;
                    this.numberFormatter = t.numberFormatter || r;
                    this.styledMode = t.styledMode;
                    this.hasCartesianSeries = t.showAxes;
                    var n = this;
                    n.index = Q.length;
                    Q.push(n);
                    f.chartCount++;
                    l && h(l, function (a, c) {
                        b(a) && L(n, c, a)
                    });
                    n.xAxis = [];
                    n.yAxis = [];
                    n.pointCount = n.colorCounter = n.symbolCounter = 0;
                    d(n, "afterInit");
                    n.firstRender()
                })
            },
            initSeries: function (a) {
                var b = this.options.chart;
                b = a.type || b.type || b.defaultSeriesType;
                var c = T[b];
                c || l(17, !0, this, {missingModuleFor: b});
                b = new c;
                b.init(this, a);
                return b
            }, setSeriesData: function () {
                this.getSeriesOrderByLinks().forEach(function (a) {
                    a.points || a.data || !a.enabledDataSorting || a.setData(a.options.data, !1)
                })
            }, getSeriesOrderByLinks: function () {
                return this.series.concat().sort(function (a, b) {
                    return a.linkedSeries.length || b.linkedSeries.length ? b.linkedSeries.length - a.linkedSeries.length : 0
                })
            }, orderSeries: function (a) {
                var b =
                    this.series;
                for (a = a || 0; a < b.length; a++) b[a] && (b[a].index = a, b[a].name = b[a].getName())
            }, isInsidePlot: function (a, b, c) {
                var h = c ? b : a;
                a = c ? a : b;
                h = {x: h, y: a, isInsidePlot: 0 <= h && h <= this.plotWidth && 0 <= a && a <= this.plotHeight};
                d(this, "afterIsInsidePlot", h);
                return h.isInsidePlot
            }, redraw: function (a) {
                d(this, "beforeRedraw");
                var b = this.axes, c = this.series, h = this.pointer, e = this.legend, m = this.userOptions.legend,
                    g = this.isDirtyLegend, t = this.hasCartesianSeries, r = this.isDirtyBox, l = this.renderer,
                    w = l.isHidden(), f = [];
                this.setResponsive &&
                this.setResponsive(!1);
                x(this.hasRendered ? a : !1, this);
                w && this.temporaryDisplay();
                this.layOutTitles();
                for (a = c.length; a--;) {
                    var z = c[a];
                    if (z.options.stacking) {
                        var K = !0;
                        if (z.isDirty) {
                            var p = !0;
                            break
                        }
                    }
                }
                if (p) for (a = c.length; a--;) z = c[a], z.options.stacking && (z.isDirty = !0);
                c.forEach(function (a) {
                    a.isDirty && ("point" === a.options.legendType ? (a.updateTotals && a.updateTotals(), g = !0) : m && (m.labelFormatter || m.labelFormat) && (g = !0));
                    a.isDirtyData && d(a, "updatedData")
                });
                g && e && e.options.enabled && (e.render(), this.isDirtyLegend =
                    !1);
                K && this.getStacks();
                t && b.forEach(function (a) {
                    a.updateNames();
                    a.setScale()
                });
                this.getMargins();
                t && (b.forEach(function (a) {
                    a.isDirty && (r = !0)
                }), b.forEach(function (a) {
                    var b = a.min + "," + a.max;
                    a.extKey !== b && (a.extKey = b, f.push(function () {
                        d(a, "afterSetExtremes", n(a.eventArgs, a.getExtremes()));
                        delete a.eventArgs
                    }));
                    (r || K) && a.redraw()
                }));
                r && this.drawChartBox();
                d(this, "predraw");
                c.forEach(function (a) {
                    (r || a.isDirty) && a.visible && a.redraw();
                    a.isDirtyData = !1
                });
                h && h.reset(!0);
                l.draw();
                d(this, "redraw");
                d(this, "render");
                w && this.temporaryDisplay(!0);
                f.forEach(function (a) {
                    a.call()
                })
            }, get: function (a) {
                function b(b) {
                    return b.id === a || b.options && b.options.id === a
                }

                var c = this.series, d;
                var h = e(this.axes, b) || e(this.series, b);
                for (d = 0; !h && d < c.length; d++) h = e(c[d].points || [], b);
                return h
            }, getAxes: function () {
                var a = this, b = this.options, c = b.xAxis = m(b.xAxis || {});
                b = b.yAxis = m(b.yAxis || {});
                d(this, "getAxes");
                c.forEach(function (a, b) {
                    a.index = b;
                    a.isX = !0
                });
                b.forEach(function (a, b) {
                    a.index = b
                });
                c.concat(b).forEach(function (b) {
                    new U(a, b)
                });
                d(this,
                    "afterGetAxes")
            }, getSelectedPoints: function () {
                var a = [];
                this.series.forEach(function (b) {
                    a = a.concat(b.getPointsCollection().filter(function (a) {
                        return t(a.selectedStaging, a.selected)
                    }))
                });
                return a
            }, getSelectedSeries: function () {
                return this.series.filter(function (a) {
                    return a.selected
                })
            }, setTitle: function (a, b, c) {
                this.applyDescription("title", a);
                this.applyDescription("subtitle", b);
                this.applyDescription("caption", void 0);
                this.layOutTitles(c)
            }, applyDescription: function (a, b) {
                var c = this, d = "title" === a ? {
                    color: "#333333",
                    fontSize: this.options.isStock ? "16px" : "18px"
                } : {color: "#666666"};
                d = this.options[a] = D(!this.styledMode && {style: d}, this.options[a], b);
                var h = this[a];
                h && b && (this[a] = h = h.destroy());
                d && !h && (h = this.renderer.text(d.text, 0, 0, d.useHTML).attr({
                    align: d.align,
                    "class": "highcharts-" + a,
                    zIndex: d.zIndex || 4
                }).add(), h.update = function (b) {
                    c[{title: "setTitle", subtitle: "setSubtitle", caption: "setCaption"}[a]](b)
                }, this.styledMode || h.css(d.style), this[a] = h)
            }, layOutTitles: function (a) {
                var b = [0, 0, 0], c = this.renderer, h = this.spacingBox;
                ["title", "subtitle", "caption"].forEach(function (a) {
                    var d = this[a], e = this.options[a], m = e.verticalAlign || "top";
                    a = "title" === a ? -3 : "top" === m ? b[0] + 2 : 0;
                    if (d) {
                        if (!this.styledMode) var g = e.style.fontSize;
                        g = c.fontMetrics(g, d).b;
                        d.css({width: (e.width || h.width + (e.widthAdjust || 0)) + "px"});
                        var t = Math.round(d.getBBox(e.useHTML).height);
                        d.align(n({y: "bottom" === m ? g : a + g, height: t}, e), !1, "spacingBox");
                        e.floating || ("top" === m ? b[0] = Math.ceil(b[0] + t) : "bottom" === m && (b[2] = Math.ceil(b[2] + t)))
                    }
                }, this);
                b[0] && "top" === (this.options.title.verticalAlign ||
                    "top") && (b[0] += this.options.title.margin);
                b[2] && "bottom" === this.options.caption.verticalAlign && (b[2] += this.options.caption.margin);
                var e = !this.titleOffset || this.titleOffset.join(",") !== b.join(",");
                this.titleOffset = b;
                d(this, "afterLayOutTitles");
                !this.isDirtyBox && e && (this.isDirtyBox = this.isDirtyLegend = e, this.hasRendered && t(a, !0) && this.isDirtyBox && this.redraw())
            }, getChartSize: function () {
                var a = this.options.chart, b = a.width;
                a = a.height;
                var c = this.renderTo;
                B(b) || (this.containerWidth = g(c, "width"));
                B(a) || (this.containerHeight =
                    g(c, "height"));
                this.chartWidth = Math.max(0, b || this.containerWidth || 600);
                this.chartHeight = Math.max(0, J(a, this.chartWidth) || (1 < this.containerHeight ? this.containerHeight : 400))
            }, temporaryDisplay: function (a) {
                var b = this.renderTo;
                if (a) for (; b && b.style;) b.hcOrigStyle && (v(b, b.hcOrigStyle), delete b.hcOrigStyle), b.hcOrigDetached && (V.body.removeChild(b), b.hcOrigDetached = !1), b = b.parentNode; else for (; b && b.style;) {
                    V.body.contains(b) || b.parentNode || (b.hcOrigDetached = !0, V.body.appendChild(b));
                    if ("none" === g(b, "display",
                        !1) || b.hcOricDetached) b.hcOrigStyle = {
                        display: b.style.display,
                        height: b.style.height,
                        overflow: b.style.overflow
                    }, a = {
                        display: "block",
                        overflow: "hidden"
                    }, b !== this.renderTo && (a.height = 0), v(b, a), b.offsetWidth || b.style.setProperty("display", "block", "important");
                    b = b.parentNode;
                    if (b === V.body) break
                }
            }, setClassName: function (a) {
                this.container.className = "highcharts-container " + (a || "")
            }, getContainer: function () {
                var b = this.options, c = b.chart;
                var h = this.renderTo;
                var e = O(), m, g;
                h || (this.renderTo = h = c.renderTo);
                z(h) && (this.renderTo =
                    h = V.getElementById(h));
                h || l(13, !0, this);
                var t = I(E(h, "data-highcharts-chart"));
                a(t) && Q[t] && Q[t].hasRendered && Q[t].destroy();
                E(h, "data-highcharts-chart", this.index);
                h.innerHTML = "";
                c.skipClone || h.offsetWidth || this.temporaryDisplay();
                this.getChartSize();
                t = this.chartWidth;
                var r = this.chartHeight;
                v(h, {overflow: "hidden"});
                this.styledMode || (m = n({
                    position: "relative",
                    overflow: "hidden",
                    width: t + "px",
                    height: r + "px",
                    textAlign: "left",
                    lineHeight: "normal",
                    zIndex: 0,
                    "-webkit-tap-highlight-color": "rgba(0,0,0,0)"
                }, c.style));
                this.container = h = A("div", {id: e}, m, h);
                this._cursor = h.style.cursor;
                this.renderer = new (f[c.renderer] || f.Renderer)(h, t, r, null, c.forExport, b.exporting && b.exporting.allowHTML, this.styledMode);
                x(void 0, this);
                this.setClassName(c.className);
                if (this.styledMode) for (g in b.defs) this.renderer.definition(b.defs[g]); else this.renderer.setStyle(c.style);
                this.renderer.chartIndex = this.index;
                d(this, "afterGetContainer")
            }, getMargins: function (a) {
                var b = this.spacing, c = this.margin, h = this.titleOffset;
                this.resetMargins();
                h[0] &&
                !B(c[0]) && (this.plotTop = Math.max(this.plotTop, h[0] + b[0]));
                h[2] && !B(c[2]) && (this.marginBottom = Math.max(this.marginBottom, h[2] + b[2]));
                this.legend && this.legend.display && this.legend.adjustMargins(c, b);
                d(this, "getMargins");
                a || this.getAxisMargins()
            }, getAxisMargins: function () {
                var a = this, b = a.axisOffset = [0, 0, 0, 0], c = a.colorAxis, d = a.margin, h = function (a) {
                    a.forEach(function (a) {
                        a.visible && a.getOffset()
                    })
                };
                a.hasCartesianSeries ? h(a.axes) : c && c.length && h(c);
                R.forEach(function (c, h) {
                    B(d[h]) || (a[c] += b[h])
                });
                a.setChartSize()
            },
            reflow: function (a) {
                var b = this, c = b.options.chart, d = b.renderTo, h = B(c.width) && B(c.height),
                    e = c.width || g(d, "width");
                c = c.height || g(d, "height");
                d = a ? a.target : M;
                if (!h && !b.isPrinting && e && c && (d === M || d === V)) {
                    if (e !== b.containerWidth || c !== b.containerHeight) u.clearTimeout(b.reflowTimeout), b.reflowTimeout = K(function () {
                        b.container && b.setSize(void 0, void 0, !1)
                    }, a ? 100 : 0);
                    b.containerWidth = e;
                    b.containerHeight = c
                }
            }, setReflow: function (a) {
                var b = this;
                !1 === a || this.unbindReflow ? !1 === a && this.unbindReflow && (this.unbindReflow =
                    this.unbindReflow()) : (this.unbindReflow = L(M, "resize", function (a) {
                    b.options && b.reflow(a)
                }), L(this, "destroy", this.unbindReflow))
            }, setSize: function (a, b, c) {
                var h = this, e = h.renderer;
                h.isResizing += 1;
                x(c, h);
                c = e.globalAnimation;
                h.oldChartHeight = h.chartHeight;
                h.oldChartWidth = h.chartWidth;
                "undefined" !== typeof a && (h.options.chart.width = a);
                "undefined" !== typeof b && (h.options.chart.height = b);
                h.getChartSize();
                h.styledMode || (c ? C : v)(h.container, {width: h.chartWidth + "px", height: h.chartHeight + "px"}, c);
                h.setChartSize(!0);
                e.setSize(h.chartWidth, h.chartHeight, c);
                h.axes.forEach(function (a) {
                    a.isDirty = !0;
                    a.setScale()
                });
                h.isDirtyLegend = !0;
                h.isDirtyBox = !0;
                h.layOutTitles();
                h.getMargins();
                h.redraw(c);
                h.oldChartHeight = null;
                d(h, "resize");
                K(function () {
                    h && d(h, "endResize", null, function () {
                        --h.isResizing
                    })
                }, F(c).duration || 0)
            }, setChartSize: function (a) {
                var b = this.inverted, c = this.renderer, h = this.chartWidth, e = this.chartHeight,
                    m = this.options.chart, g = this.spacing, t = this.clipOffset, r, l, w, n;
                this.plotLeft = r = Math.round(this.plotLeft);
                this.plotTop =
                    l = Math.round(this.plotTop);
                this.plotWidth = w = Math.max(0, Math.round(h - r - this.marginRight));
                this.plotHeight = n = Math.max(0, Math.round(e - l - this.marginBottom));
                this.plotSizeX = b ? n : w;
                this.plotSizeY = b ? w : n;
                this.plotBorderWidth = m.plotBorderWidth || 0;
                this.spacingBox = c.spacingBox = {x: g[3], y: g[0], width: h - g[3] - g[1], height: e - g[0] - g[2]};
                this.plotBox = c.plotBox = {x: r, y: l, width: w, height: n};
                h = 2 * Math.floor(this.plotBorderWidth / 2);
                b = Math.ceil(Math.max(h, t[3]) / 2);
                c = Math.ceil(Math.max(h, t[0]) / 2);
                this.clipBox = {
                    x: b,
                    y: c,
                    width: Math.floor(this.plotSizeX -
                        Math.max(h, t[1]) / 2 - b),
                    height: Math.max(0, Math.floor(this.plotSizeY - Math.max(h, t[2]) / 2 - c))
                };
                a || this.axes.forEach(function (a) {
                    a.setAxisSize();
                    a.setAxisTranslation()
                });
                d(this, "afterSetChartSize", {skipAxes: a})
            }, resetMargins: function () {
                d(this, "resetMargins");
                var a = this, b = a.options.chart;
                ["margin", "spacing"].forEach(function (c) {
                    var d = b[c], h = w(d) ? d : [d, d, d, d];
                    ["Top", "Right", "Bottom", "Left"].forEach(function (d, e) {
                        a[c][e] = t(b[c + d], h[e])
                    })
                });
                R.forEach(function (b, c) {
                    a[b] = t(a.margin[c], a.spacing[c])
                });
                a.axisOffset =
                    [0, 0, 0, 0];
                a.clipOffset = [0, 0, 0, 0]
            }, drawChartBox: function () {
                var a = this.options.chart, b = this.renderer, c = this.chartWidth, h = this.chartHeight,
                    e = this.chartBackground, m = this.plotBackground, g = this.plotBorder, t = this.styledMode,
                    r = this.plotBGImage, l = a.backgroundColor, w = a.plotBackgroundColor, n = a.plotBackgroundImage,
                    x, f = this.plotLeft, z = this.plotTop, K = this.plotWidth, p = this.plotHeight, O = this.plotBox,
                    I = this.clipRect, J = this.clipBox, D = "animate";
                e || (this.chartBackground = e = b.rect().addClass("highcharts-background").add(),
                    D = "attr");
                if (t) var v = x = e.strokeWidth(); else {
                    v = a.borderWidth || 0;
                    x = v + (a.shadow ? 8 : 0);
                    l = {fill: l || "none"};
                    if (v || e["stroke-width"]) l.stroke = a.borderColor, l["stroke-width"] = v;
                    e.attr(l).shadow(a.shadow)
                }
                e[D]({x: x / 2, y: x / 2, width: c - x - v % 2, height: h - x - v % 2, r: a.borderRadius});
                D = "animate";
                m || (D = "attr", this.plotBackground = m = b.rect().addClass("highcharts-plot-background").add());
                m[D](O);
                t || (m.attr({fill: w || "none"}).shadow(a.plotShadow), n && (r ? (n !== r.attr("href") && r.attr("href", n), r.animate(O)) : this.plotBGImage = b.image(n,
                    f, z, K, p).add()));
                I ? I.animate({width: J.width, height: J.height}) : this.clipRect = b.clipRect(J);
                D = "animate";
                g || (D = "attr", this.plotBorder = g = b.rect().addClass("highcharts-plot-border").attr({zIndex: 1}).add());
                t || g.attr({stroke: a.plotBorderColor, "stroke-width": a.plotBorderWidth || 0, fill: "none"});
                g[D](g.crisp({x: f, y: z, width: K, height: p}, -g.strokeWidth()));
                this.isDirtyBox = !1;
                d(this, "afterDrawChartBox")
            }, propFromSeries: function () {
                var a = this, b = a.options.chart, c, d = a.options.series, h, e;
                ["inverted", "angular", "polar"].forEach(function (m) {
                    c =
                        T[b.type || b.defaultSeriesType];
                    e = b[m] || c && c.prototype[m];
                    for (h = d && d.length; !e && h--;) (c = T[d[h].type]) && c.prototype[m] && (e = !0);
                    a[m] = e
                })
            }, linkSeries: function () {
                var a = this, b = a.series;
                b.forEach(function (a) {
                    a.linkedSeries.length = 0
                });
                b.forEach(function (b) {
                    var c = b.options.linkedTo;
                    z(c) && (c = ":previous" === c ? a.series[b.index - 1] : a.get(c)) && c.linkedParent !== b && (c.linkedSeries.push(b), b.linkedParent = c, c.enabledDataSorting && b.setDataSortingOptions(), b.visible = t(b.options.visible, c.options.visible, b.visible))
                });
                d(this, "afterLinkSeries")
            }, renderSeries: function () {
                this.series.forEach(function (a) {
                    a.translate();
                    a.render()
                })
            }, renderLabels: function () {
                var a = this, b = a.options.labels;
                b.items && b.items.forEach(function (c) {
                    var d = n(b.style, c.style), h = I(d.left) + a.plotLeft, e = I(d.top) + a.plotTop + 12;
                    delete d.left;
                    delete d.top;
                    a.renderer.text(c.html, h, e).attr({zIndex: 2}).css(d).add()
                })
            }, render: function () {
                var a = this.axes, b = this.colorAxis, c = this.renderer, d = this.options, h = 0, e = function (a) {
                    a.forEach(function (a) {
                        a.visible && a.render()
                    })
                };
                this.setTitle();
                this.legend = new k(this, d.legend);
                this.getStacks && this.getStacks();
                this.getMargins(!0);
                this.setChartSize();
                d = this.plotWidth;
                a.some(function (a) {
                    if (a.horiz && a.visible && a.options.labels.enabled && a.series.length) return h = 21, !0
                });
                var m = this.plotHeight = Math.max(this.plotHeight - h, 0);
                a.forEach(function (a) {
                    a.setScale()
                });
                this.getAxisMargins();
                var g = 1.1 < d / this.plotWidth;
                var t = 1.05 < m / this.plotHeight;
                if (g || t) a.forEach(function (a) {
                    (a.horiz && g || !a.horiz && t) && a.setTickInterval(!0)
                }), this.getMargins();
                this.drawChartBox();
                this.hasCartesianSeries ? e(a) : b && b.length && e(b);
                this.seriesGroup || (this.seriesGroup = c.g("series-group").attr({zIndex: 3}).add());
                this.renderSeries();
                this.renderLabels();
                this.addCredits();
                this.setResponsive && this.setResponsive();
                this.updateContainerScaling();
                this.hasRendered = !0
            }, addCredits: function (a) {
                var b = this;
                a = D(!0, this.options.credits, a);
                a.enabled && !this.credits && (this.credits = this.renderer.text(a.text + (this.mapCredits || ""), 0, 0).addClass("highcharts-credits").on("click", function () {
                    a.href &&
                    (M.location.href = a.href)
                }).attr({
                    align: a.position.align,
                    zIndex: 8
                }), b.styledMode || this.credits.css(a.style), this.credits.add().align(a.position), this.credits.update = function (a) {
                    b.credits = b.credits.destroy();
                    b.addCredits(a)
                })
            }, updateContainerScaling: function () {
                var a = this.container;
                if (a.offsetWidth && a.offsetHeight && a.getBoundingClientRect) {
                    var b = a.getBoundingClientRect(), c = b.width / a.offsetWidth;
                    a = b.height / a.offsetHeight;
                    1 !== c || 1 !== a ? this.containerScaling = {scaleX: c, scaleY: a} : delete this.containerScaling
                }
            },
            destroy: function () {
                var a = this, b = a.axes, c = a.series, e = a.container, m, g = e && e.parentNode;
                d(a, "destroy");
                a.renderer.forExport ? p(Q, a) : Q[a.index] = void 0;
                f.chartCount--;
                a.renderTo.removeAttribute("data-highcharts-chart");
                N(a);
                for (m = b.length; m--;) b[m] = b[m].destroy();
                this.scroller && this.scroller.destroy && this.scroller.destroy();
                for (m = c.length; m--;) c[m] = c[m].destroy();
                "title subtitle chartBackground plotBackground plotBGImage plotBorder seriesGroup clipRect credits pointer rangeSelector legend resetZoomButton tooltip renderer".split(" ").forEach(function (b) {
                    var c =
                        a[b];
                    c && c.destroy && (a[b] = c.destroy())
                });
                e && (e.innerHTML = "", N(e), g && y(e));
                h(a, function (b, c) {
                    delete a[c]
                })
            }, firstRender: function () {
                var a = this, b = a.options;
                if (!a.isReadyToRender || a.isReadyToRender()) {
                    a.getContainer();
                    a.resetMargins();
                    a.setChartSize();
                    a.propFromSeries();
                    a.getAxes();
                    (c(b.series) ? b.series : []).forEach(function (b) {
                        a.initSeries(b)
                    });
                    a.linkSeries();
                    a.setSeriesData();
                    d(a, "beforeRender");
                    q && (a.pointer = f.hasTouch || !M.PointerEvent && !M.MSPointerEvent ? new q(a, b) : new H(a, b));
                    a.render();
                    if (!a.renderer.imgCount &&
                        !a.hasLoaded) a.onload();
                    a.temporaryDisplay(!0)
                }
            }, onload: function () {
                this.callbacks.concat([this.callback]).forEach(function (a) {
                    a && "undefined" !== typeof this.index && a.apply(this, [this])
                }, this);
                d(this, "load");
                d(this, "render");
                B(this.index) && this.setReflow(this.options.chart.reflow);
                this.hasLoaded = !0
            }
        })
    });
    P(u, "parts/ScrollablePlotArea.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent, q = k.createElement, G = k.pick, M = k.stop;
        k = f.Chart;
        "";
        u(k, "afterSetChartSize", function (k) {
            var q =
                this.options.chart.scrollablePlotArea, F = q && q.minWidth;
            q = q && q.minHeight;
            if (!this.renderer.forExport) {
                if (F) {
                    if (this.scrollablePixelsX = F = Math.max(0, F - this.chartWidth)) {
                        this.plotWidth += F;
                        this.inverted ? (this.clipBox.height += F, this.plotBox.height += F) : (this.clipBox.width += F, this.plotBox.width += F);
                        var E = {1: {name: "right", value: F}}
                    }
                } else q && (this.scrollablePixelsY = F = Math.max(0, q - this.chartHeight)) && (this.plotHeight += F, this.inverted ? (this.clipBox.width += F, this.plotBox.width += F) : (this.clipBox.height += F, this.plotBox.height +=
                    F), E = {2: {name: "bottom", value: F}});
                E && !k.skipAxes && this.axes.forEach(function (k) {
                    E[k.side] ? k.getPlotLinePath = function () {
                        var v = E[k.side].name, A = this[v];
                        this[v] = A - E[k.side].value;
                        var y = f.Axis.prototype.getPlotLinePath.apply(this, arguments);
                        this[v] = A;
                        return y
                    } : (k.setAxisSize(), k.setAxisTranslation())
                })
            }
        });
        u(k, "render", function () {
            this.scrollablePixelsX || this.scrollablePixelsY ? (this.setUpScrolling && this.setUpScrolling(), this.applyFixed()) : this.fixedDiv && this.applyFixed()
        });
        k.prototype.setUpScrolling = function () {
            var f =
                this, k = {WebkitOverflowScrolling: "touch", overflowX: "hidden", overflowY: "hidden"};
            this.scrollablePixelsX && (k.overflowX = "auto");
            this.scrollablePixelsY && (k.overflowY = "auto");
            this.scrollingContainer = q("div", {className: "highcharts-scrolling"}, k, this.renderTo);
            u(this.scrollingContainer, "scroll", function () {
                f.pointer && delete f.pointer.chartPosition
            });
            this.innerContainer = q("div", {className: "highcharts-inner-container"}, null, this.scrollingContainer);
            this.innerContainer.appendChild(this.container);
            this.setUpScrolling =
                null
        };
        k.prototype.moveFixedElements = function () {
            var f = this.container, k = this.fixedRenderer,
                q = ".highcharts-contextbutton .highcharts-credits .highcharts-legend .highcharts-legend-checkbox .highcharts-navigator-series .highcharts-navigator-xaxis .highcharts-navigator-yaxis .highcharts-navigator .highcharts-reset-zoom .highcharts-scrollbar .highcharts-subtitle .highcharts-title".split(" "),
                E;
            this.scrollablePixelsX && !this.inverted ? E = ".highcharts-yaxis" : this.scrollablePixelsX && this.inverted ? E = ".highcharts-xaxis" :
                this.scrollablePixelsY && !this.inverted ? E = ".highcharts-xaxis" : this.scrollablePixelsY && this.inverted && (E = ".highcharts-yaxis");
            q.push(E, E + "-labels");
            q.forEach(function (A) {
                [].forEach.call(f.querySelectorAll(A), function (f) {
                    (f.namespaceURI === k.SVG_NS ? k.box : k.box.parentNode).appendChild(f);
                    f.style.pointerEvents = "auto"
                })
            })
        };
        k.prototype.applyFixed = function () {
            var k, C = !this.fixedDiv, F = this.options.chart.scrollablePlotArea;
            C ? (this.fixedDiv = q("div", {className: "highcharts-fixed"}, {
                position: "absolute", overflow: "hidden",
                pointerEvents: "none", zIndex: 2
            }, null, !0), this.renderTo.insertBefore(this.fixedDiv, this.renderTo.firstChild), this.renderTo.style.overflow = "visible", this.fixedRenderer = k = new f.Renderer(this.fixedDiv, this.chartWidth, this.chartHeight), this.scrollableMask = k.path().attr({
                fill: this.options.chart.backgroundColor || "#fff",
                "fill-opacity": G(F.opacity, .85),
                zIndex: -1
            }).addClass("highcharts-scrollable-mask").add(), this.moveFixedElements(), u(this, "afterShowResetZoom", this.moveFixedElements), u(this, "afterLayOutTitles",
                this.moveFixedElements)) : this.fixedRenderer.setSize(this.chartWidth, this.chartHeight);
            k = this.chartWidth + (this.scrollablePixelsX || 0);
            var E = this.chartHeight + (this.scrollablePixelsY || 0);
            M(this.container);
            this.container.style.width = k + "px";
            this.container.style.height = E + "px";
            this.renderer.boxWrapper.attr({width: k, height: E, viewBox: [0, 0, k, E].join(" ")});
            this.chartBackground.attr({width: k, height: E});
            this.scrollablePixelsY && (this.scrollingContainer.style.height = this.chartHeight + "px");
            C && (F.scrollPositionX &&
            (this.scrollingContainer.scrollLeft = this.scrollablePixelsX * F.scrollPositionX), F.scrollPositionY && (this.scrollingContainer.scrollTop = this.scrollablePixelsY * F.scrollPositionY));
            E = this.axisOffset;
            C = this.plotTop - E[0] - 1;
            F = this.plotLeft - E[3] - 1;
            k = this.plotTop + this.plotHeight + E[2] + 1;
            E = this.plotLeft + this.plotWidth + E[1] + 1;
            var A = this.plotLeft + this.plotWidth - (this.scrollablePixelsX || 0),
                v = this.plotTop + this.plotHeight - (this.scrollablePixelsY || 0);
            C = this.scrollablePixelsX ? ["M", 0, C, "L", this.plotLeft - 1, C, "L", this.plotLeft -
            1, k, "L", 0, k, "Z", "M", A, C, "L", this.chartWidth, C, "L", this.chartWidth, k, "L", A, k, "Z"] : this.scrollablePixelsY ? ["M", F, 0, "L", F, this.plotTop - 1, "L", E, this.plotTop - 1, "L", E, 0, "Z", "M", F, v, "L", F, this.chartHeight, "L", E, this.chartHeight, "L", E, v, "Z"] : ["M", 0, 0];
            "adjustHeight" !== this.redrawTrigger && this.scrollableMask.attr({d: C})
        }
    });
    P(u, "mixins/legend-symbol.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.merge, q = k.pick;
        f.LegendSymbolMixin = {
            drawRectangle: function (f, k) {
                var u = f.symbolHeight,
                    C = f.options.squareSymbol;
                k.legendSymbol = this.chart.renderer.rect(C ? (f.symbolWidth - u) / 2 : 0, f.baseline - u + 1, C ? u : f.symbolWidth, u, q(f.options.symbolRadius, u / 2)).addClass("highcharts-point").attr({zIndex: 3}).add(k.legendGroup)
            }, drawLineMarker: function (f) {
                var k = this.options, H = k.marker, C = f.symbolWidth, F = f.symbolHeight, E = F / 2,
                    A = this.chart.renderer, v = this.legendGroup;
                f = f.baseline - Math.round(.3 * f.fontMetrics.b);
                var B = {};
                this.chart.styledMode || (B = {"stroke-width": k.lineWidth || 0}, k.dashStyle && (B.dashstyle = k.dashStyle));
                this.legendLine = A.path(["M", 0, f, "L", C, f]).addClass("highcharts-graph").attr(B).add(v);
                H && !1 !== H.enabled && C && (k = Math.min(q(H.radius, E), E), 0 === this.symbol.indexOf("url") && (H = u(H, {
                    width: F,
                    height: F
                }), k = 0), this.legendSymbol = H = A.symbol(this.symbol, C / 2 - k, f - k, 2 * k, 2 * k, H).addClass("highcharts-point").add(v), H.isMarker = !0)
            }
        };
        return f.LegendSymbolMixin
    });
    P(u, "parts/Point.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        "";
        var u = k.animObject, q = k.defined, G = k.erase, M = k.extend, L = k.format, C = k.getNestedProperty,
            F = k.isArray, E = k.isNumber, A = k.isObject, v = k.syncTimeout, B = k.pick, y = k.removeEvent,
            p = k.uniqueKey, l = f.fireEvent;
        k = function () {
            function n() {
                this.colorIndex = this.category = void 0;
                this.formatPrefix = "point";
                this.id = void 0;
                this.isNull = !1;
                this.percentage = this.options = this.name = void 0;
                this.selected = !1;
                this.total = this.series = void 0;
                this.visible = !0;
                this.x = void 0
            }

            n.prototype.animateBeforeDestroy = function () {
                var e = this, d = {x: e.startXPos, opacity: 0}, g, c = e.getGraphicalProps();
                c.singular.forEach(function (b) {
                    g = "dataLabel" ===
                        b;
                    e[b] = e[b].animate(g ? {x: e[b].startXPos, y: e[b].startYPos, opacity: 0} : d)
                });
                c.plural.forEach(function (b) {
                    e[b].forEach(function (a) {
                        a.element && a.animate(M({x: e.startXPos}, a.startYPos ? {x: a.startXPos, y: a.startYPos} : {}))
                    })
                })
            };
            n.prototype.applyOptions = function (e, d) {
                var g = this.series, c = g.options.pointValKey || g.pointValKey;
                e = n.prototype.optionsToObject.call(this, e);
                M(this, e);
                this.options = this.options ? M(this.options, e) : e;
                e.group && delete this.group;
                e.dataLabels && delete this.dataLabels;
                c && (this.y = n.prototype.getNestedProperty.call(this,
                    c));
                this.formatPrefix = (this.isNull = B(this.isValid && !this.isValid(), null === this.x || !E(this.y))) ? "null" : "point";
                this.selected && (this.state = "select");
                "name" in this && "undefined" === typeof d && g.xAxis && g.xAxis.hasNames && (this.x = g.xAxis.nameToX(this));
                "undefined" === typeof this.x && g && (this.x = "undefined" === typeof d ? g.autoIncrement(this) : d);
                return this
            };
            n.prototype.destroy = function () {
                function e() {
                    if (d.graphic || d.dataLabel || d.dataLabels) y(d), d.destroyElements();
                    for (l in d) d[l] = null
                }

                var d = this, g = d.series, c = g.chart;
                g = g.options.dataSorting;
                var b = c.hoverPoints, a = u(d.series.chart.renderer.globalAnimation), l;
                d.legendItem && c.legend.destroyItem(d);
                b && (d.setState(), G(b, d), b.length || (c.hoverPoints = null));
                if (d === c.hoverPoint) d.onMouseOut();
                g && g.enabled ? (this.animateBeforeDestroy(), v(e, a.duration)) : e();
                c.pointCount--
            };
            n.prototype.destroyElements = function (e) {
                var d = this;
                e = d.getGraphicalProps(e);
                e.singular.forEach(function (e) {
                    d[e] = d[e].destroy()
                });
                e.plural.forEach(function (e) {
                    d[e].forEach(function (c) {
                        c.element && c.destroy()
                    });
                    delete d[e]
                })
            };
            n.prototype.firePointEvent = function (e, d, g) {
                var c = this, b = this.series.options;
                (b.point.events[e] || c.options && c.options.events && c.options.events[e]) && c.importEvents();
                "click" === e && b.allowPointSelect && (g = function (a) {
                    c.select && c.select(null, a.ctrlKey || a.metaKey || a.shiftKey)
                });
                l(c, e, d, g)
            };
            n.prototype.getClassName = function () {
                return "highcharts-point" + (this.selected ? " highcharts-point-select" : "") + (this.negative ? " highcharts-negative" : "") + (this.isNull ? " highcharts-null-point" : "") + ("undefined" !==
                typeof this.colorIndex ? " highcharts-color-" + this.colorIndex : "") + (this.options.className ? " " + this.options.className : "") + (this.zone && this.zone.className ? " " + this.zone.className.replace("highcharts-negative", "") : "")
            };
            n.prototype.getGraphicalProps = function (e) {
                var d = this, g = [], c, b = {singular: [], plural: []};
                e = e || {graphic: 1, dataLabel: 1};
                e.graphic && g.push("graphic", "shadowGroup");
                e.dataLabel && g.push("dataLabel", "dataLabelUpper", "connector");
                for (c = g.length; c--;) {
                    var a = g[c];
                    d[a] && b.singular.push(a)
                }
                ["dataLabel",
                    "connector"].forEach(function (a) {
                    var c = a + "s";
                    e[a] && d[c] && b.plural.push(c)
                });
                return b
            };
            n.prototype.getLabelConfig = function () {
                return {
                    x: this.category,
                    y: this.y,
                    color: this.color,
                    colorIndex: this.colorIndex,
                    key: this.name || this.category,
                    series: this.series,
                    point: this,
                    percentage: this.percentage,
                    total: this.total || this.stackTotal
                }
            };
            n.prototype.getNestedProperty = function (e) {
                if (e) return 0 === e.indexOf("custom.") ? C(e, this.options) : this[e]
            };
            n.prototype.getZone = function () {
                var e = this.series, d = e.zones;
                e = e.zoneAxis ||
                    "y";
                var g = 0, c;
                for (c = d[g]; this[e] >= c.value;) c = d[++g];
                this.nonZonedColor || (this.nonZonedColor = this.color);
                this.color = c && c.color && !this.options.color ? c.color : this.nonZonedColor;
                return c
            };
            n.prototype.hasNewShapeType = function () {
                return (this.graphic && (this.graphic.symbolName || this.graphic.element.nodeName)) !== this.shapeType
            };
            n.prototype.init = function (e, d, g) {
                this.series = e;
                this.applyOptions(d, g);
                this.id = q(this.id) ? this.id : p();
                this.resolveColor();
                e.chart.pointCount++;
                l(this, "afterInit");
                return this
            };
            n.prototype.optionsToObject =
                function (e) {
                    var d = {}, g = this.series, c = g.options.keys, b = c || g.pointArrayMap || ["y"], a = b.length,
                        l = 0, f = 0;
                    if (E(e) || null === e) d[b[0]] = e; else if (F(e)) for (!c && e.length > a && (g = typeof e[0], "string" === g ? d.name = e[0] : "number" === g && (d.x = e[0]), l++); f < a;) c && "undefined" === typeof e[l] || (0 < b[f].indexOf(".") ? n.prototype.setNestedProperty(d, e[l], b[f]) : d[b[f]] = e[l]), l++, f++; else "object" === typeof e && (d = e, e.dataLabels && (g._hasPointLabels = !0), e.marker && (g._hasPointMarkers = !0));
                    return d
                };
            n.prototype.resolveColor = function () {
                var e =
                    this.series;
                var d = e.chart.options.chart.colorCount;
                var g = e.chart.styledMode;
                g || this.options.color || (this.color = e.color);
                e.options.colorByPoint ? (g || (d = e.options.colors || e.chart.options.colors, this.color = this.color || d[e.colorCounter], d = d.length), g = e.colorCounter, e.colorCounter++, e.colorCounter === d && (e.colorCounter = 0)) : g = e.colorIndex;
                this.colorIndex = B(this.colorIndex, g)
            };
            n.prototype.setNestedProperty = function (e, d, g) {
                g.split(".").reduce(function (c, b, a, e) {
                        c[b] = e.length - 1 === a ? d : A(c[b], !0) ? c[b] : {};
                        return c[b]
                    },
                    e);
                return e
            };
            n.prototype.tooltipFormatter = function (e) {
                var d = this.series, g = d.tooltipOptions, c = B(g.valueDecimals, ""), b = g.valuePrefix || "",
                    a = g.valueSuffix || "";
                d.chart.styledMode && (e = d.chart.tooltip.styledModeFormat(e));
                (d.pointArrayMap || ["y"]).forEach(function (d) {
                    d = "{point." + d;
                    if (b || a) e = e.replace(RegExp(d + "}", "g"), b + d + "}" + a);
                    e = e.replace(RegExp(d + "}", "g"), d + ":,." + c + "f}")
                });
                return L(e, {point: this, series: this.series}, d.chart)
            };
            return n
        }();
        f.Point = k;
        return f.Point
    });
    P(u, "parts/Series.js", [u["parts/Globals.js"],
        u["mixins/legend-symbol.js"], u["parts/Point.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        "";
        var H = q.addEvent, M = q.animObject, L = q.arrayMax, C = q.arrayMin, F = q.clamp, E = q.correctFloat,
            A = q.defined, v = q.erase, B = q.error, y = q.extend, p = q.find, l = q.fireEvent, n = q.getNestedProperty,
            e = q.isArray, d = q.isFunction, g = q.isNumber, c = q.isString, b = q.merge, a = q.objectEach, w = q.pick,
            z = q.removeEvent, D = q.seriesType, r = q.splat, h = q.syncTimeout, t = f.defaultOptions,
            I = f.defaultPlotOptions, J = f.seriesTypes, N = f.SVGElement, x = f.win;
        f.Series =
            D("line", null, {
                lineWidth: 2,
                allowPointSelect: !1,
                showCheckbox: !1,
                animation: {duration: 1E3},
                events: {},
                marker: {
                    enabledThreshold: 2,
                    lineColor: "#ffffff",
                    lineWidth: 0,
                    radius: 4,
                    states: {
                        normal: {animation: !0},
                        hover: {animation: {duration: 50}, enabled: !0, radiusPlus: 2, lineWidthPlus: 1},
                        select: {fillColor: "#cccccc", lineColor: "#000000", lineWidth: 2}
                    }
                },
                point: {events: {}},
                dataLabels: {
                    align: "center", formatter: function () {
                        var a = this.series.chart.numberFormatter;
                        return "number" !== typeof this.y ? "" : a(this.y, -1)
                    }, padding: 5, style: {
                        fontSize: "11px",
                        fontWeight: "bold", color: "contrast", textOutline: "1px contrast"
                    }, verticalAlign: "bottom", x: 0, y: 0
                },
                cropThreshold: 300,
                opacity: 1,
                pointRange: 0,
                softThreshold: !0,
                states: {
                    normal: {animation: !0},
                    hover: {animation: {duration: 50}, lineWidthPlus: 1, marker: {}, halo: {size: 10, opacity: .25}},
                    select: {animation: {duration: 0}},
                    inactive: {animation: {duration: 50}, opacity: .2}
                },
                stickyTracking: !0,
                turboThreshold: 1E3,
                findNearestPointBy: "x"
            }, {
                axisTypes: ["xAxis", "yAxis"],
                coll: "series",
                colorCounter: 0,
                cropShoulder: 1,
                directTouch: !1,
                eventsToUnbind: [],
                isCartesian: !0,
                parallelArrays: ["x", "y"],
                pointClass: u,
                requireSorting: !0,
                sorted: !0,
                init: function (b, c) {
                    l(this, "init", {options: c});
                    var h = this, e = b.series, m;
                    this.eventOptions = this.eventOptions || {};
                    h.chart = b;
                    h.options = c = h.setOptions(c);
                    h.linkedSeries = [];
                    h.bindAxes();
                    y(h, {name: c.name, state: "", visible: !1 !== c.visible, selected: !0 === c.selected});
                    var g = c.events;
                    a(g, function (a, b) {
                        d(a) && h.eventOptions[b] !== a && (d(h.eventOptions[b]) && z(h, b, h.eventOptions[b]), h.eventOptions[b] = a, H(h, b, a))
                    });
                    if (g && g.click || c.point &&
                        c.point.events && c.point.events.click || c.allowPointSelect) b.runTrackerClick = !0;
                    h.getColor();
                    h.getSymbol();
                    h.parallelArrays.forEach(function (a) {
                        h[a + "Data"] || (h[a + "Data"] = [])
                    });
                    h.isCartesian && (b.hasCartesianSeries = !0);
                    e.length && (m = e[e.length - 1]);
                    h._i = w(m && m._i, -1) + 1;
                    b.orderSeries(this.insert(e));
                    c.dataSorting && c.dataSorting.enabled ? h.setDataSortingOptions() : h.points || h.data || h.setData(c.data, !1);
                    l(this, "afterInit")
                },
                is: function (a) {
                    return J[a] && this instanceof J[a]
                },
                insert: function (a) {
                    var b = this.options.index,
                        c;
                    if (g(b)) {
                        for (c = a.length; c--;) if (b >= w(a[c].options.index, a[c]._i)) {
                            a.splice(c + 1, 0, this);
                            break
                        }
                        -1 === c && a.unshift(this);
                        c += 1
                    } else a.push(this);
                    return w(c, a.length - 1)
                },
                bindAxes: function () {
                    var a = this, b = a.options, c = a.chart, d;
                    l(this, "bindAxes", null, function () {
                        (a.axisTypes || []).forEach(function (h) {
                            c[h].forEach(function (c) {
                                d = c.options;
                                if (b[h] === d.index || "undefined" !== typeof b[h] && b[h] === d.id || "undefined" === typeof b[h] && 0 === d.index) a.insert(c.series), a[h] = c, c.isDirty = !0
                            });
                            a[h] || a.optionalAxis === h || B(18, !0,
                                c)
                        })
                    });
                    l(this, "afterBindAxes")
                },
                updateParallelArrays: function (a, b) {
                    var c = a.series, d = arguments, h = g(b) ? function (d) {
                        var h = "y" === d && c.toYData ? c.toYData(a) : a[d];
                        c[d + "Data"][b] = h
                    } : function (a) {
                        Array.prototype[b].apply(c[a + "Data"], Array.prototype.slice.call(d, 2))
                    };
                    c.parallelArrays.forEach(h)
                },
                hasData: function () {
                    return this.visible && "undefined" !== typeof this.dataMax && "undefined" !== typeof this.dataMin || this.visible && this.yData && 0 < this.yData.length
                },
                autoIncrement: function () {
                    var a = this.options, b = this.xIncrement,
                        c, d = a.pointIntervalUnit, h = this.chart.time;
                    b = w(b, a.pointStart, 0);
                    this.pointInterval = c = w(this.pointInterval, a.pointInterval, 1);
                    d && (a = new h.Date(b), "day" === d ? h.set("Date", a, h.get("Date", a) + c) : "month" === d ? h.set("Month", a, h.get("Month", a) + c) : "year" === d && h.set("FullYear", a, h.get("FullYear", a) + c), c = a.getTime() - b);
                    this.xIncrement = b + c;
                    return b
                },
                setDataSortingOptions: function () {
                    var a = this.options;
                    y(this, {requireSorting: !1, sorted: !1, enabledDataSorting: !0, allowDG: !1});
                    A(a.pointRange) || (a.pointRange = 1)
                },
                setOptions: function (a) {
                    var c =
                        this.chart, d = c.options, h = d.plotOptions, e = c.userOptions || {};
                    a = b(a);
                    c = c.styledMode;
                    var m = {plotOptions: h, userOptions: a};
                    l(this, "setOptions", m);
                    var g = m.plotOptions[this.type], r = e.plotOptions || {};
                    this.userOptions = m.userOptions;
                    e = b(g, h.series, e.plotOptions && e.plotOptions[this.type], a);
                    this.tooltipOptions = b(t.tooltip, t.plotOptions.series && t.plotOptions.series.tooltip, t.plotOptions[this.type].tooltip, d.tooltip.userOptions, h.series && h.series.tooltip, h[this.type].tooltip, a.tooltip);
                    this.stickyTracking = w(a.stickyTracking,
                        r[this.type] && r[this.type].stickyTracking, r.series && r.series.stickyTracking, this.tooltipOptions.shared && !this.noSharedTooltip ? !0 : e.stickyTracking);
                    null === g.marker && delete e.marker;
                    this.zoneAxis = e.zoneAxis;
                    d = this.zones = (e.zones || []).slice();
                    !e.negativeColor && !e.negativeFillColor || e.zones || (h = {
                        value: e[this.zoneAxis + "Threshold"] || e.threshold || 0,
                        className: "highcharts-negative"
                    }, c || (h.color = e.negativeColor, h.fillColor = e.negativeFillColor), d.push(h));
                    d.length && A(d[d.length - 1].value) && d.push(c ? {} : {
                        color: this.color,
                        fillColor: this.fillColor
                    });
                    l(this, "afterSetOptions", {options: e});
                    return e
                },
                getName: function () {
                    return w(this.options.name, "Series " + (this.index + 1))
                },
                getCyclic: function (a, b, c) {
                    var d = this.chart, h = this.userOptions, e = a + "Index", m = a + "Counter",
                        g = c ? c.length : w(d.options.chart[a + "Count"], d[a + "Count"]);
                    if (!b) {
                        var t = w(h[e], h["_" + e]);
                        A(t) || (d.series.length || (d[m] = 0), h["_" + e] = t = d[m] % g, d[m] += 1);
                        c && (b = c[t])
                    }
                    "undefined" !== typeof t && (this[e] = t);
                    this[a] = b
                },
                getColor: function () {
                    this.chart.styledMode ? this.getCyclic("color") :
                        this.options.colorByPoint ? this.options.color = null : this.getCyclic("color", this.options.color || I[this.type].color, this.chart.options.colors)
                },
                getPointsCollection: function () {
                    return (this.hasGroupedData ? this.points : this.data) || []
                },
                getSymbol: function () {
                    this.getCyclic("symbol", this.options.marker.symbol, this.chart.options.symbols)
                },
                findPointIndex: function (a, b) {
                    var c = a.id, d = a.x, h = this.points, e, m = this.options.dataSorting;
                    if (c) var t = this.chart.get(c); else if (this.linkedParent || this.enabledDataSorting) {
                        var r =
                            m && m.matchByName ? "name" : "index";
                        t = p(h, function (b) {
                            return !b.touched && b[r] === a[r]
                        });
                        if (!t) return
                    }
                    if (t) {
                        var l = t && t.index;
                        "undefined" !== typeof l && (e = !0)
                    }
                    "undefined" === typeof l && g(d) && (l = this.xData.indexOf(d, b));
                    -1 !== l && "undefined" !== typeof l && this.cropped && (l = l >= this.cropStart ? l - this.cropStart : l);
                    !e && h[l] && h[l].touched && (l = void 0);
                    return l
                },
                drawLegendSymbol: k.drawLineMarker,
                updateData: function (a, b) {
                    var c = this.options, d = c.dataSorting, h = this.points, e = [], m, t, r, l = this.requireSorting,
                        n = a.length === h.length,
                        w = !0;
                    this.xIncrement = null;
                    a.forEach(function (a, b) {
                        var t = A(a) && this.pointClass.prototype.optionsToObject.call({series: this}, a) || {};
                        var w = t.x;
                        if (t.id || g(w)) {
                            if (w = this.findPointIndex(t, r), -1 === w || "undefined" === typeof w ? e.push(a) : h[w] && a !== c.data[w] ? (h[w].update(a, !1, null, !1), h[w].touched = !0, l && (r = w + 1)) : h[w] && (h[w].touched = !0), !n || b !== w || d && d.enabled || this.hasDerivedData) m = !0
                        } else e.push(a)
                    }, this);
                    if (m) for (a = h.length; a--;) (t = h[a]) && !t.touched && t.remove && t.remove(!1, b); else !n || d && d.enabled ? w = !1 : (a.forEach(function (a,
                                                                                                                                                                       b) {
                        h[b].update && a !== h[b].y && h[b].update(a, !1, null, !1)
                    }), e.length = 0);
                    h.forEach(function (a) {
                        a && (a.touched = !1)
                    });
                    if (!w) return !1;
                    e.forEach(function (a) {
                        this.addPoint(a, !1, null, null, !1)
                    }, this);
                    null === this.xIncrement && this.xData && this.xData.length && (this.xIncrement = L(this.xData), this.autoIncrement());
                    return !0
                },
                setData: function (a, b, d, h) {
                    var m = this, t = m.points, r = t && t.length || 0, l, n = m.options, f = m.chart,
                        x = n.dataSorting, z = null, p = m.xAxis;
                    z = n.turboThreshold;
                    var K = this.xData, I = this.yData, k = (l = m.pointArrayMap) && l.length,
                        J = n.keys, D = 0, v = 1, N;
                    a = a || [];
                    l = a.length;
                    b = w(b, !0);
                    x && x.enabled && (a = this.sortData(a));
                    !1 !== h && l && r && !m.cropped && !m.hasGroupedData && m.visible && !m.isSeriesBoosting && (N = this.updateData(a, d));
                    if (!N) {
                        m.xIncrement = null;
                        m.colorCounter = 0;
                        this.parallelArrays.forEach(function (a) {
                            m[a + "Data"].length = 0
                        });
                        if (z && l > z) if (z = m.getFirstValidPoint(a), g(z)) for (d = 0; d < l; d++) K[d] = this.autoIncrement(), I[d] = a[d]; else if (e(z)) if (k) for (d = 0; d < l; d++) h = a[d], K[d] = h[0], I[d] = h.slice(1, k + 1); else for (J && (D = J.indexOf("x"), v = J.indexOf("y"),
                            D = 0 <= D ? D : 0, v = 0 <= v ? v : 1), d = 0; d < l; d++) h = a[d], K[d] = h[D], I[d] = h[v]; else B(12, !1, f); else for (d = 0; d < l; d++) "undefined" !== typeof a[d] && (h = {series: m}, m.pointClass.prototype.applyOptions.apply(h, [a[d]]), m.updateParallelArrays(h, d));
                        I && c(I[0]) && B(14, !0, f);
                        m.data = [];
                        m.options.data = m.userOptions.data = a;
                        for (d = r; d--;) t[d] && t[d].destroy && t[d].destroy();
                        p && (p.minRange = p.userMinRange);
                        m.isDirty = f.isDirtyBox = !0;
                        m.isDirtyData = !!t;
                        d = !1
                    }
                    "point" === n.legendType && (this.processData(), this.generatePoints());
                    b && f.redraw(d)
                },
                sortData: function (a) {
                    var b = this, c = b.options.dataSorting.sortKey || "y", d = function (a, b) {
                        return A(b) && a.pointClass.prototype.optionsToObject.call({series: a}, b) || {}
                    };
                    a.forEach(function (c, h) {
                        a[h] = d(b, c);
                        a[h].index = h
                    }, this);
                    a.concat().sort(function (a, b) {
                        a = n(c, a);
                        b = n(c, b);
                        return b < a ? -1 : b > a ? 1 : 0
                    }).forEach(function (a, b) {
                        a.x = b
                    }, this);
                    b.linkedSeries && b.linkedSeries.forEach(function (b) {
                        var c = b.options, h = c.data;
                        c.dataSorting && c.dataSorting.enabled || !h || (h.forEach(function (c, e) {
                            h[e] = d(b, c);
                            a[e] && (h[e].x = a[e].x, h[e].index =
                                e)
                        }), b.setData(h, !1))
                    });
                    return a
                },
                processData: function (a) {
                    var b = this.xData, c = this.yData, d = b.length;
                    var h = 0;
                    var e = this.xAxis, m = this.options;
                    var g = m.cropThreshold;
                    var t = this.getExtremesFromAll || m.getExtremesFromAll, r = this.isCartesian;
                    m = e && e.val2lin;
                    var l = e && e.isLog, w = this.requireSorting;
                    if (r && !this.isDirty && !e.isDirty && !this.yAxis.isDirty && !a) return !1;
                    if (e) {
                        a = e.getExtremes();
                        var n = a.min;
                        var f = a.max
                    }
                    if (r && this.sorted && !t && (!g || d > g || this.forceCrop)) if (b[d - 1] < n || b[0] > f) b = [], c = []; else if (this.yData && (b[0] <
                        n || b[d - 1] > f)) {
                        h = this.cropData(this.xData, this.yData, n, f);
                        b = h.xData;
                        c = h.yData;
                        h = h.start;
                        var x = !0
                    }
                    for (g = b.length || 1; --g;) if (d = l ? m(b[g]) - m(b[g - 1]) : b[g] - b[g - 1], 0 < d && ("undefined" === typeof z || d < z)) var z = d; else 0 > d && w && (B(15, !1, this.chart), w = !1);
                    this.cropped = x;
                    this.cropStart = h;
                    this.processedXData = b;
                    this.processedYData = c;
                    this.closestPointRange = this.basePointRange = z
                },
                cropData: function (a, b, c, d, h) {
                    var e = a.length, m = 0, g = e, t;
                    h = w(h, this.cropShoulder);
                    for (t = 0; t < e; t++) if (a[t] >= c) {
                        m = Math.max(0, t - h);
                        break
                    }
                    for (c = t; c <
                    e; c++) if (a[c] > d) {
                        g = c + h;
                        break
                    }
                    return {xData: a.slice(m, g), yData: b.slice(m, g), start: m, end: g}
                },
                generatePoints: function () {
                    var a = this.options, b = a.data, c = this.data, d, h = this.processedXData,
                        e = this.processedYData, g = this.pointClass, t = h.length, w = this.cropStart || 0,
                        n = this.hasGroupedData;
                    a = a.keys;
                    var f = [], x;
                    c || n || (c = [], c.length = b.length, c = this.data = c);
                    a && n && (this.options.keys = !1);
                    for (x = 0; x < t; x++) {
                        var z = w + x;
                        if (n) {
                            var p = (new g).init(this, [h[x]].concat(r(e[x])));
                            p.dataGroup = this.groupMap[x];
                            p.dataGroup.options && (p.options =
                                p.dataGroup.options, y(p, p.dataGroup.options), delete p.dataLabels)
                        } else (p = c[z]) || "undefined" === typeof b[z] || (c[z] = p = (new g).init(this, b[z], h[x]));
                        p && (p.index = z, f[x] = p)
                    }
                    this.options.keys = a;
                    if (c && (t !== (d = c.length) || n)) for (x = 0; x < d; x++) x !== w || n || (x += t), c[x] && (c[x].destroyElements(), c[x].plotX = void 0);
                    this.data = c;
                    this.points = f;
                    l(this, "afterGeneratePoints")
                },
                getXExtremes: function (a) {
                    return {min: C(a), max: L(a)}
                },
                getExtremes: function (a) {
                    var b = this.xAxis, c = this.yAxis, d = this.processedXData || this.xData, h = [], m =
                        0, t = 0;
                    var r = 0;
                    var n = this.requireSorting ? this.cropShoulder : 0, w = c ? c.positiveValuesOnly : !1, f;
                    a = a || this.stackedYData || this.processedYData || [];
                    c = a.length;
                    b && (r = b.getExtremes(), t = r.min, r = r.max);
                    for (f = 0; f < c; f++) {
                        var x = d[f];
                        var z = a[f];
                        var p = (g(z) || e(z)) && (z.length || 0 < z || !w);
                        x = this.getExtremesFromAll || this.options.getExtremesFromAll || this.cropped || !b || (d[f + n] || x) >= t && (d[f - n] || x) <= r;
                        if (p && x) if (p = z.length) for (; p--;) g(z[p]) && (h[m++] = z[p]); else h[m++] = z
                    }
                    this.dataMin = C(h);
                    this.dataMax = L(h);
                    l(this, "afterGetExtremes")
                },
                getFirstValidPoint: function (a) {
                    for (var b = null, c = a.length, d = 0; null === b && d < c;) b = a[d], d++;
                    return b
                },
                translate: function () {
                    this.processedXData || this.processData();
                    this.generatePoints();
                    var a = this.options, b = a.stacking, c = this.xAxis, d = c.categories, h = this.enabledDataSorting,
                        t = this.yAxis, r = this.points, n = r.length, f = !!this.modifyValue, x,
                        z = this.pointPlacementToXValue(), p = !!z, I = a.threshold, k = a.startFromThreshold ? I : 0,
                        J, D = this.zoneAxis || "y", v = Number.MAX_VALUE;
                    for (x = 0; x < n; x++) {
                        var N = r[x], y = N.x, B = N.y, q = N.low, C = b &&
                            t.stacks[(this.negStacks && B < (k ? 0 : I) ? "-" : "") + this.stackKey];
                        t.positiveValuesOnly && null !== B && 0 >= B && (N.isNull = !0);
                        N.plotX = J = E(F(c.translate(y, 0, 0, 0, 1, z, "flags" === this.type), -1E5, 1E5));
                        if (b && this.visible && C && C[y]) {
                            var u = this.getStackIndicator(u, y, this.index);
                            if (!N.isNull) {
                                var H = C[y];
                                var G = H.points[u.key]
                            }
                        }
                        e(G) && (q = G[0], B = G[1], q === k && u.key === C[y].base && (q = w(g(I) && I, t.min)), t.positiveValuesOnly && 0 >= q && (q = null), N.total = N.stackTotal = H.total, N.percentage = H.total && N.y / H.total * 100, N.stackY = B, this.irregularWidths ||
                        H.setOffset(this.pointXOffset || 0, this.barW || 0));
                        N.yBottom = A(q) ? F(t.translate(q, 0, 1, 0, 1), -1E5, 1E5) : null;
                        f && (B = this.modifyValue(B, N));
                        N.plotY = "number" === typeof B && Infinity !== B ? F(t.translate(B, 0, 1, 0, 1), -1E5, 1E5) : void 0;
                        N.isInside = this.isPointInside(N);
                        N.clientX = p ? E(c.translate(y, 0, 0, 0, 1, z)) : J;
                        N.negative = N[D] < (a[D + "Threshold"] || I || 0);
                        N.category = d && "undefined" !== typeof d[N.x] ? d[N.x] : N.x;
                        if (!N.isNull && !1 !== N.visible) {
                            "undefined" !== typeof L && (v = Math.min(v, Math.abs(J - L)));
                            var L = J
                        }
                        N.zone = this.zones.length &&
                            N.getZone();
                        !N.graphic && this.group && h && (N.isNew = !0)
                    }
                    this.closestPointRangePx = v;
                    l(this, "afterTranslate")
                },
                getValidPoints: function (a, b, c) {
                    var d = this.chart;
                    return (a || this.points || []).filter(function (a) {
                        return b && !d.isInsidePlot(a.plotX, a.plotY, d.inverted) ? !1 : !1 !== a.visible && (c || !a.isNull)
                    })
                },
                getClipBox: function (a, b) {
                    var c = this.options, d = this.chart, h = d.inverted, e = this.xAxis, m = e && this.yAxis;
                    a && !1 === c.clip && m ? a = h ? {
                        y: -d.chartWidth + m.len + m.pos,
                        height: d.chartWidth,
                        width: d.chartHeight,
                        x: -d.chartHeight + e.len +
                            e.pos
                    } : {
                        y: -m.pos,
                        height: d.chartHeight,
                        width: d.chartWidth,
                        x: -e.pos
                    } : (a = this.clipBox || d.clipBox, b && (a.width = d.plotSizeX, a.x = 0));
                    return b ? {width: a.width, x: a.x} : a
                },
                setClip: function (a) {
                    var b = this.chart, c = this.options, d = b.renderer, h = b.inverted, e = this.clipBox,
                        m = this.getClipBox(a),
                        g = this.sharedClipKey || ["_sharedClip", a && a.duration, a && a.easing, m.height, c.xAxis, c.yAxis].join(),
                        t = b[g], r = b[g + "m"];
                    a && (m.width = 0, h && (m.x = b.plotHeight + (!1 !== c.clip ? 0 : b.plotTop)));
                    t ? b.hasLoaded || t.attr(m) : (a && (b[g + "m"] = r = d.clipRect(h ?
                        b.plotSizeX + 99 : -99, h ? -b.plotLeft : -b.plotTop, 99, h ? b.chartWidth : b.chartHeight)), b[g] = t = d.clipRect(m), t.count = {length: 0});
                    a && !t.count[this.index] && (t.count[this.index] = !0, t.count.length += 1);
                    if (!1 !== c.clip || a) this.group.clip(a || e ? t : b.clipRect), this.markerGroup.clip(r), this.sharedClipKey = g;
                    a || (t.count[this.index] && (delete t.count[this.index], --t.count.length), 0 === t.count.length && g && b[g] && (e || (b[g] = b[g].destroy()), b[g + "m"] && (b[g + "m"] = b[g + "m"].destroy())))
                },
                animate: function (a) {
                    var b = this.chart, c = M(this.options.animation);
                    if (!b.hasRendered) if (a) this.setClip(c); else {
                        var d = this.sharedClipKey;
                        a = b[d];
                        var h = this.getClipBox(c, !0);
                        a && a.animate(h, c);
                        b[d + "m"] && b[d + "m"].animate({width: h.width + 99, x: h.x - (b.inverted ? 0 : 99)}, c)
                    }
                },
                afterAnimate: function () {
                    this.setClip();
                    l(this, "afterAnimate");
                    this.finishedAnimating = !0
                },
                drawPoints: function () {
                    var a = this.points, b = this.chart, c, d, h = this.options.marker,
                        e = this[this.specialGroup] || this.markerGroup, g = this.xAxis,
                        t = w(h.enabled, !g || g.isRadial ? !0 : null, this.closestPointRangePx >= h.enabledThreshold *
                            h.radius);
                    if (!1 !== h.enabled || this._hasPointMarkers) for (c = 0; c < a.length; c++) {
                        var r = a[c];
                        var l = (d = r.graphic) ? "animate" : "attr";
                        var n = r.marker || {};
                        var f = !!r.marker;
                        if ((t && "undefined" === typeof n.enabled || n.enabled) && !r.isNull && !1 !== r.visible) {
                            var x = w(n.symbol, this.symbol);
                            var z = this.markerAttribs(r, r.selected && "select");
                            this.enabledDataSorting && (r.startXPos = g.reversed ? -z.width : g.width);
                            var p = !1 !== r.isInside;
                            d ? d[p ? "show" : "hide"](p).animate(z) : p && (0 < z.width || r.hasImage) && (r.graphic = d = b.renderer.symbol(x,
                                z.x, z.y, z.width, z.height, f ? n : h).add(e), this.enabledDataSorting && b.hasRendered && (d.attr({x: r.startXPos}), l = "animate"));
                            d && "animate" === l && d[p ? "show" : "hide"](p).animate(z);
                            if (d && !b.styledMode) d[l](this.pointAttribs(r, r.selected && "select"));
                            d && d.addClass(r.getClassName(), !0)
                        } else d && (r.graphic = d.destroy())
                    }
                },
                markerAttribs: function (a, b) {
                    var c = this.options.marker, d = a.marker || {}, h = d.symbol || c.symbol,
                        e = w(d.radius, c.radius);
                    b && (c = c.states[b], b = d.states && d.states[b], e = w(b && b.radius, c && c.radius, e + (c && c.radiusPlus ||
                        0)));
                    a.hasImage = h && 0 === h.indexOf("url");
                    a.hasImage && (e = 0);
                    a = {x: Math.floor(a.plotX) - e, y: a.plotY - e};
                    e && (a.width = a.height = 2 * e);
                    return a
                },
                pointAttribs: function (a, b) {
                    var c = this.options.marker, d = a && a.options, h = d && d.marker || {}, e = this.color,
                        g = d && d.color, m = a && a.color;
                    d = w(h.lineWidth, c.lineWidth);
                    var t = a && a.zone && a.zone.color;
                    a = 1;
                    e = g || t || m || e;
                    g = h.fillColor || c.fillColor || e;
                    e = h.lineColor || c.lineColor || e;
                    b = b || "normal";
                    c = c.states[b];
                    b = h.states && h.states[b] || {};
                    d = w(b.lineWidth, c.lineWidth, d + w(b.lineWidthPlus, c.lineWidthPlus,
                        0));
                    g = b.fillColor || c.fillColor || g;
                    e = b.lineColor || c.lineColor || e;
                    a = w(b.opacity, c.opacity, a);
                    return {stroke: e, "stroke-width": d, fill: g, opacity: a}
                },
                destroy: function (b) {
                    var c = this, d = c.chart, h = /AppleWebKit\/533/.test(x.navigator.userAgent), e, g,
                        m = c.data || [], t, r;
                    l(c, "destroy");
                    this.removeEvents(b);
                    (c.axisTypes || []).forEach(function (a) {
                        (r = c[a]) && r.series && (v(r.series, c), r.isDirty = r.forceRedraw = !0)
                    });
                    c.legendItem && c.chart.legend.destroyItem(c);
                    for (g = m.length; g--;) (t = m[g]) && t.destroy && t.destroy();
                    c.points = null;
                    q.clearTimeout(c.animationTimeout);
                    a(c, function (a, b) {
                        a instanceof N && !a.survive && (e = h && "group" === b ? "hide" : "destroy", a[e]())
                    });
                    d.hoverSeries === c && (d.hoverSeries = null);
                    v(d.series, c);
                    d.orderSeries();
                    a(c, function (a, d) {
                        b && "hcEvents" === d || delete c[d]
                    })
                },
                getGraphPath: function (a, b, c) {
                    var d = this, h = d.options, e = h.step, g, m = [], t = [], r;
                    a = a || d.points;
                    (g = a.reversed) && a.reverse();
                    (e = {right: 1, center: 2}[e] || e && 3) && g && (e = 4 - e);
                    a = this.getValidPoints(a, !1, !(h.connectNulls && !b && !c));
                    a.forEach(function (g, l) {
                        var n = g.plotX,
                            w = g.plotY, f = a[l - 1];
                        (g.leftCliff || f && f.rightCliff) && !c && (r = !0);
                        g.isNull && !A(b) && 0 < l ? r = !h.connectNulls : g.isNull && !b ? r = !0 : (0 === l || r ? l = ["M", g.plotX, g.plotY] : d.getPointSpline ? l = d.getPointSpline(a, g, l) : e ? (l = 1 === e ? ["L", f.plotX, w] : 2 === e ? ["L", (f.plotX + n) / 2, f.plotY, "L", (f.plotX + n) / 2, w] : ["L", n, f.plotY], l.push("L", n, w)) : l = ["L", n, w], t.push(g.x), e && (t.push(g.x), 2 === e && t.push(g.x)), m.push.apply(m, l), r = !1)
                    });
                    m.xMap = t;
                    return d.graphPath = m
                },
                drawGraph: function () {
                    var a = this, b = this.options, c = (this.gappedPath || this.getGraphPath).call(this),
                        d = this.chart.styledMode, h = [["graph", "highcharts-graph"]];
                    d || h[0].push(b.lineColor || this.color || "#cccccc", b.dashStyle);
                    h = a.getZonesGraphs(h);
                    h.forEach(function (h, e) {
                        var g = h[0], m = a[g], t = m ? "animate" : "attr";
                        m ? (m.endX = a.preventGraphAnimation ? null : c.xMap, m.animate({d: c})) : c.length && (a[g] = m = a.chart.renderer.path(c).addClass(h[1]).attr({zIndex: 1}).add(a.group));
                        m && !d && (g = {
                            stroke: h[2],
                            "stroke-width": b.lineWidth,
                            fill: a.fillGraph && a.color || "none"
                        }, h[3] ? g.dashstyle = h[3] : "square" !== b.linecap && (g["stroke-linecap"] =
                            g["stroke-linejoin"] = "round"), m[t](g).shadow(2 > e && b.shadow));
                        m && (m.startX = c.xMap, m.isArea = c.isArea)
                    })
                },
                getZonesGraphs: function (a) {
                    this.zones.forEach(function (b, c) {
                        c = ["zone-graph-" + c, "highcharts-graph highcharts-zone-graph-" + c + " " + (b.className || "")];
                        this.chart.styledMode || c.push(b.color || this.color, b.dashStyle || this.options.dashStyle);
                        a.push(c)
                    }, this);
                    return a
                },
                applyZones: function () {
                    var a = this, b = this.chart, c = b.renderer, d = this.zones, h, e, g = this.clips || [], t,
                        r = this.graph, l = this.area, n = Math.max(b.chartWidth,
                        b.chartHeight), f = this[(this.zoneAxis || "y") + "Axis"], x = b.inverted, z, p, I, k = !1;
                    if (d.length && (r || l) && f && "undefined" !== typeof f.min) {
                        var J = f.reversed;
                        var D = f.horiz;
                        r && !this.showLine && r.hide();
                        l && l.hide();
                        var v = f.getExtremes();
                        d.forEach(function (d, m) {
                            h = J ? D ? b.plotWidth : 0 : D ? 0 : f.toPixels(v.min) || 0;
                            h = F(w(e, h), 0, n);
                            e = F(Math.round(f.toPixels(w(d.value, v.max), !0) || 0), 0, n);
                            k && (h = e = f.toPixels(v.max));
                            z = Math.abs(h - e);
                            p = Math.min(h, e);
                            I = Math.max(h, e);
                            f.isXAxis ? (t = {x: x ? I : p, y: 0, width: z, height: n}, D || (t.x = b.plotHeight -
                                t.x)) : (t = {x: 0, y: x ? I : p, width: n, height: z}, D && (t.y = b.plotWidth - t.y));
                            x && c.isVML && (t = f.isXAxis ? {
                                x: 0,
                                y: J ? p : I,
                                height: t.width,
                                width: b.chartWidth
                            } : {x: t.y - b.plotLeft - b.spacingBox.x, y: 0, width: t.height, height: b.chartHeight});
                            g[m] ? g[m].animate(t) : g[m] = c.clipRect(t);
                            r && a["zone-graph-" + m].clip(g[m]);
                            l && a["zone-area-" + m].clip(g[m]);
                            k = d.value > v.max;
                            a.resetZones && 0 === e && (e = void 0)
                        });
                        this.clips = g
                    } else a.visible && (r && r.show(!0), l && l.show(!0))
                },
                invertGroups: function (a) {
                    function b() {
                        ["group", "markerGroup"].forEach(function (b) {
                            c[b] &&
                            (d.renderer.isVML && c[b].attr({
                                width: c.yAxis.len,
                                height: c.xAxis.len
                            }), c[b].width = c.yAxis.len, c[b].height = c.xAxis.len, c[b].invert(c.isRadialSeries ? !1 : a))
                        })
                    }

                    var c = this, d = c.chart;
                    c.xAxis && (c.eventsToUnbind.push(H(d, "resize", b)), b(), c.invertGroups = b)
                },
                plotGroup: function (a, b, c, d, h) {
                    var e = this[a], g = !e;
                    g && (this[a] = e = this.chart.renderer.g().attr({zIndex: d || .1}).add(h));
                    e.addClass("highcharts-" + b + " highcharts-series-" + this.index + " highcharts-" + this.type + "-series " + (A(this.colorIndex) ? "highcharts-color-" + this.colorIndex +
                        " " : "") + (this.options.className || "") + (e.hasClass("highcharts-tracker") ? " highcharts-tracker" : ""), !0);
                    e.attr({visibility: c})[g ? "attr" : "animate"](this.getPlotBox());
                    return e
                },
                getPlotBox: function () {
                    var a = this.chart, b = this.xAxis, c = this.yAxis;
                    a.inverted && (b = c, c = this.xAxis);
                    return {
                        translateX: b ? b.left : a.plotLeft,
                        translateY: c ? c.top : a.plotTop,
                        scaleX: 1,
                        scaleY: 1
                    }
                },
                removeEvents: function (a) {
                    a ? this.eventsToUnbind.length && (this.eventsToUnbind.forEach(function (a) {
                        a()
                    }), this.eventsToUnbind.length = 0) : z(this)
                },
                render: function () {
                    var a =
                            this, b = a.chart, c = a.options,
                        d = !a.finishedAnimating && b.renderer.isSVG && M(c.animation).duration,
                        e = a.visible ? "inherit" : "hidden", g = c.zIndex, t = a.hasRendered, r = b.seriesGroup,
                        n = b.inverted;
                    l(this, "render");
                    var f = a.plotGroup("group", "series", e, g, r);
                    a.markerGroup = a.plotGroup("markerGroup", "markers", e, g, r);
                    d && a.animate && a.animate(!0);
                    f.inverted = a.isCartesian || a.invertable ? n : !1;
                    a.drawGraph && (a.drawGraph(), a.applyZones());
                    a.visible && a.drawPoints();
                    a.drawDataLabels && a.drawDataLabels();
                    a.redrawPoints && a.redrawPoints();
                    a.drawTracker && !1 !== a.options.enableMouseTracking && a.drawTracker();
                    a.invertGroups(n);
                    !1 === c.clip || a.sharedClipKey || t || f.clip(b.clipRect);
                    d && a.animate && a.animate();
                    t || (a.animationTimeout = h(function () {
                        a.afterAnimate()
                    }, d || 0));
                    a.isDirty = !1;
                    a.hasRendered = !0;
                    l(a, "afterRender")
                },
                redraw: function () {
                    var a = this.chart, b = this.isDirty || this.isDirtyData, c = this.group, d = this.xAxis,
                        h = this.yAxis;
                    c && (a.inverted && c.attr({width: a.plotWidth, height: a.plotHeight}), c.animate({
                        translateX: w(d && d.left, a.plotLeft), translateY: w(h &&
                            h.top, a.plotTop)
                    }));
                    this.translate();
                    this.render();
                    b && delete this.kdTree
                },
                kdAxisArray: ["clientX", "plotY"],
                searchPoint: function (a, b) {
                    var c = this.xAxis, d = this.yAxis, h = this.chart.inverted;
                    return this.searchKDTree({
                        clientX: h ? c.len - a.chartY + c.pos : a.chartX - c.pos,
                        plotY: h ? d.len - a.chartX + d.pos : a.chartY - d.pos
                    }, b, a)
                },
                buildKDTree: function (a) {
                    function b(a, d, h) {
                        var e;
                        if (e = a && a.length) {
                            var g = c.kdAxisArray[d % h];
                            a.sort(function (a, b) {
                                return a[g] - b[g]
                            });
                            e = Math.floor(e / 2);
                            return {
                                point: a[e], left: b(a.slice(0, e), d + 1, h),
                                right: b(a.slice(e + 1), d + 1, h)
                            }
                        }
                    }

                    this.buildingKdTree = !0;
                    var c = this, d = -1 < c.options.findNearestPointBy.indexOf("y") ? 2 : 1;
                    delete c.kdTree;
                    h(function () {
                        c.kdTree = b(c.getValidPoints(null, !c.directTouch), d, d);
                        c.buildingKdTree = !1
                    }, c.options.kdNow || a && "touchstart" === a.type ? 0 : 1)
                },
                searchKDTree: function (a, b, c) {
                    function d(a, b, c, t) {
                        var r = b.point, l = h.kdAxisArray[c % t], n = r;
                        var f = A(a[e]) && A(r[e]) ? Math.pow(a[e] - r[e], 2) : null;
                        var w = A(a[g]) && A(r[g]) ? Math.pow(a[g] - r[g], 2) : null;
                        w = (f || 0) + (w || 0);
                        r.dist = A(w) ? Math.sqrt(w) : Number.MAX_VALUE;
                        r.distX = A(f) ? Math.sqrt(f) : Number.MAX_VALUE;
                        l = a[l] - r[l];
                        w = 0 > l ? "left" : "right";
                        f = 0 > l ? "right" : "left";
                        b[w] && (w = d(a, b[w], c + 1, t), n = w[m] < n[m] ? w : r);
                        b[f] && Math.sqrt(l * l) < n[m] && (a = d(a, b[f], c + 1, t), n = a[m] < n[m] ? a : n);
                        return n
                    }

                    var h = this, e = this.kdAxisArray[0], g = this.kdAxisArray[1], m = b ? "distX" : "dist";
                    b = -1 < h.options.findNearestPointBy.indexOf("y") ? 2 : 1;
                    this.kdTree || this.buildingKdTree || this.buildKDTree(c);
                    if (this.kdTree) return d(a, this.kdTree, b, b)
                },
                pointPlacementToXValue: function () {
                    var a = this.options, b = a.pointRange,
                        c = this.xAxis;
                    a = a.pointPlacement;
                    "between" === a && (a = c.reversed ? -.5 : .5);
                    return g(a) ? a * w(b, c.pointRange) : 0
                },
                isPointInside: function (a) {
                    return "undefined" !== typeof a.plotY && "undefined" !== typeof a.plotX && 0 <= a.plotY && a.plotY <= this.yAxis.len && 0 <= a.plotX && a.plotX <= this.xAxis.len
                }
            });
        ""
    });
    P(u, "parts/Stacking.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.correctFloat, q = k.defined, G = k.destroyObjectProperties, M = k.format, L = k.objectEach,
            C = k.pick;
        k = f.Axis;
        var F = f.Chart, E = f.Series;
        f.StackItem =
            function (f, k, B, y, p) {
                var l = f.chart.inverted;
                this.axis = f;
                this.isNegative = B;
                this.options = k = k || {};
                this.x = y;
                this.total = null;
                this.points = {};
                this.stack = p;
                this.rightCliff = this.leftCliff = 0;
                this.alignOptions = {
                    align: k.align || (l ? B ? "left" : "right" : "center"),
                    verticalAlign: k.verticalAlign || (l ? "middle" : B ? "bottom" : "top"),
                    y: k.y,
                    x: k.x
                };
                this.textAlign = k.textAlign || (l ? B ? "right" : "left" : "center")
            };
        f.StackItem.prototype = {
            destroy: function () {
                G(this, this.axis)
            }, render: function (f) {
                var k = this.axis.chart, A = this.options, y = A.format;
                y = y ? M(y, this, k) : A.formatter.call(this);
                this.label ? this.label.attr({
                    text: y,
                    visibility: "hidden"
                }) : (this.label = k.renderer.label(y, null, null, A.shape, null, null, A.useHTML, !1, "stack-labels"), y = {
                    text: y,
                    rotation: A.rotation,
                    padding: C(A.padding, 5),
                    visibility: "hidden"
                }, this.label.attr(y), k.styledMode || this.label.css(A.style), this.label.added || this.label.add(f));
                this.label.labelrank = k.plotHeight
            }, setOffset: function (f, k, B, y, p) {
                var l = this.axis, n = l.chart;
                y = l.translate(l.usePercentage ? 100 : y ? y : this.total, 0, 0, 0, 1);
                B = l.translate(B ? B : 0);
                B = q(y) && Math.abs(y - B);
                f = C(p, n.xAxis[0].translate(this.x)) + f;
                l = q(y) && this.getStackBox(n, this, f, y, k, B, l);
                k = this.label;
                B = this.isNegative;
                f = "justify" === C(this.options.overflow, "justify");
                var e = this.textAlign;
                k && l && (p = k.getBBox(), y = k.padding, e = "left" === e ? n.inverted ? -y : y : "right" === e ? p.width : n.inverted && "center" === e ? p.width / 2 : n.inverted ? B ? p.width + y : -y : p.width / 2, B = n.inverted ? p.height / 2 : B ? -y : p.height, this.alignOptions.x = C(this.options.x, 0), this.alignOptions.y = C(this.options.y, 0), l.x -=
                    e, l.y -= B, k.align(this.alignOptions, null, l), n.isInsidePlot(k.alignAttr.x + e - this.alignOptions.x, k.alignAttr.y + B - this.alignOptions.y) ? k.show() : (k.alignAttr.y = -9999, f = !1), f && E.prototype.justifyDataLabel.call(this.axis, k, this.alignOptions, k.alignAttr, p, l), k.attr({
                    x: k.alignAttr.x,
                    y: k.alignAttr.y
                }), C(!f && this.options.crop, !0) && ((n = n.isInsidePlot(k.x - y + k.width, k.y) && n.isInsidePlot(k.x + y, k.y)) || k.hide()))
            }, getStackBox: function (f, k, B, y, p, l, n) {
                var e = k.axis.reversed, d = f.inverted;
                f = n.height + n.pos - (d ? f.plotLeft :
                    f.plotTop);
                k = k.isNegative && !e || !k.isNegative && e;
                return {
                    x: d ? k ? y : y - l : B,
                    y: d ? f - B - p : k ? f - y - l : f - y,
                    width: d ? l : p,
                    height: d ? p : l
                }
            }
        };
        F.prototype.getStacks = function () {
            var f = this, k = f.inverted;
            f.yAxis.forEach(function (f) {
                f.stacks && f.hasVisibleSeries && (f.oldStacks = f.stacks)
            });
            f.series.forEach(function (v) {
                var A = v.xAxis && v.xAxis.options || {};
                !v.options.stacking || !0 !== v.visible && !1 !== f.options.chart.ignoreHiddenSeries || (v.stackKey = [v.type, C(v.options.stack, ""), k ? A.top : A.left, k ? A.height : A.width].join())
            })
        };
        k.prototype.buildStacks =
            function () {
                var k = this.series, v = C(this.options.reversedStacks, !0), q = k.length, y;
                if (!this.isXAxis) {
                    this.usePercentage = !1;
                    for (y = q; y--;) {
                        var p = k[v ? y : q - y - 1];
                        p.setStackedPoints()
                    }
                    for (y = 0; y < q; y++) k[y].modifyStacks();
                    f.fireEvent(this, "afterBuildStacks")
                }
            };
        k.prototype.renderStackTotals = function () {
            var f = this.chart, k = f.renderer, q = this.stacks, y = this.stackTotalGroup;
            y || (this.stackTotalGroup = y = k.g("stack-labels").attr({visibility: "visible", zIndex: 6}).add());
            y.translate(f.plotLeft, f.plotTop);
            L(q, function (f) {
                L(f,
                    function (l) {
                        l.render(y)
                    })
            })
        };
        k.prototype.resetStacks = function () {
            var f = this, k = f.stacks;
            f.isXAxis || L(k, function (k) {
                L(k, function (v, p) {
                    v.touched < f.stacksTouched ? (v.destroy(), delete k[p]) : (v.total = null, v.cumulative = null)
                })
            })
        };
        k.prototype.cleanStacks = function () {
            if (!this.isXAxis) {
                if (this.oldStacks) var f = this.stacks = this.oldStacks;
                L(f, function (f) {
                    L(f, function (f) {
                        f.cumulative = f.total
                    })
                })
            }
        };
        E.prototype.setStackedPoints = function () {
            if (this.options.stacking && (!0 === this.visible || !1 === this.chart.options.chart.ignoreHiddenSeries)) {
                var k =
                        this.processedXData, v = this.processedYData, B = [], y = v.length, p = this.options,
                    l = p.threshold, n = C(p.startFromThreshold && l, 0), e = p.stack;
                p = p.stacking;
                var d = this.stackKey, g = "-" + d, c = this.negStacks, b = this.yAxis, a = b.stacks, w = b.oldStacks,
                    z, D;
                b.stacksTouched += 1;
                for (D = 0; D < y; D++) {
                    var r = k[D];
                    var h = v[D];
                    var t = this.getStackIndicator(t, r, this.index);
                    var I = t.key;
                    var J = (z = c && h < (n ? 0 : l)) ? g : d;
                    a[J] || (a[J] = {});
                    a[J][r] || (w[J] && w[J][r] ? (a[J][r] = w[J][r], a[J][r].total = null) : a[J][r] = new f.StackItem(b, b.options.stackLabels, z, r, e));
                    J = a[J][r];
                    null !== h ? (J.points[I] = J.points[this.index] = [C(J.cumulative, n)], q(J.cumulative) || (J.base = I), J.touched = b.stacksTouched, 0 < t.index && !1 === this.singleStacks && (J.points[I][0] = J.points[this.index + "," + r + ",0"][0])) : J.points[I] = J.points[this.index] = null;
                    "percent" === p ? (z = z ? d : g, c && a[z] && a[z][r] ? (z = a[z][r], J.total = z.total = Math.max(z.total, J.total) + Math.abs(h) || 0) : J.total = u(J.total + (Math.abs(h) || 0))) : J.total = u(J.total + (h || 0));
                    J.cumulative = C(J.cumulative, n) + (h || 0);
                    null !== h && (J.points[I].push(J.cumulative),
                        B[D] = J.cumulative)
                }
                "percent" === p && (b.usePercentage = !0);
                this.stackedYData = B;
                b.oldStacks = {}
            }
        };
        E.prototype.modifyStacks = function () {
            var f = this, k = f.stackKey, q = f.yAxis.stacks, y = f.processedXData, p, l = f.options.stacking;
            f[l + "Stacker"] && [k, "-" + k].forEach(function (n) {
                for (var e = y.length, d, g; e--;) if (d = y[e], p = f.getStackIndicator(p, d, f.index, n), g = (d = q[n] && q[n][d]) && d.points[p.key]) f[l + "Stacker"](g, d, e)
            })
        };
        E.prototype.percentStacker = function (f, k, q) {
            k = k.total ? 100 / k.total : 0;
            f[0] = u(f[0] * k);
            f[1] = u(f[1] * k);
            this.stackedYData[q] =
                f[1]
        };
        E.prototype.getStackIndicator = function (f, k, B, y) {
            !q(f) || f.x !== k || y && f.key !== y ? f = {x: k, index: 0, key: y} : f.index++;
            f.key = [B, k, f.index].join();
            return f
        }
    });
    P(u, "parts/Dynamics.js", [u["parts/Globals.js"], u["parts/Point.js"], u["parts/Time.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        var H = q.addEvent, M = q.animate, L = q.createElement, C = q.css, F = q.defined, E = q.erase, A = q.error,
            v = q.extend, B = q.fireEvent, y = q.isArray, p = q.isNumber, l = q.isObject, n = q.isString, e = q.merge,
            d = q.objectEach, g = q.pick, c = q.relativeLength, b =
                q.setAnimation, a = q.splat, w = f.Axis;
        q = f.Chart;
        var z = f.Series, D = f.seriesTypes;
        f.cleanRecursively = function (a, b) {
            var c = {};
            d(a, function (d, h) {
                if (l(a[h], !0) && !a.nodeType && b[h]) d = f.cleanRecursively(a[h], b[h]), Object.keys(d).length && (c[h] = d); else if (l(a[h]) || a[h] !== b[h]) c[h] = a[h]
            });
            return c
        };
        v(q.prototype, {
            addSeries: function (a, b, c) {
                var d, h = this;
                a && (b = g(b, !0), B(h, "addSeries", {options: a}, function () {
                    d = h.initSeries(a);
                    h.isDirtyLegend = !0;
                    h.linkSeries();
                    d.enabledDataSorting && d.setData(a.data, !1);
                    B(h, "afterAddSeries",
                        {series: d});
                    b && h.redraw(c)
                }));
                return d
            },
            addAxis: function (a, b, c, d) {
                return this.createAxis(b ? "xAxis" : "yAxis", {axis: a, redraw: c, animation: d})
            },
            addColorAxis: function (a, b, c) {
                return this.createAxis("colorAxis", {axis: a, redraw: b, animation: c})
            },
            createAxis: function (b, c) {
                var d = this.options, h = "colorAxis" === b, r = c.redraw, l = c.animation;
                c = e(c.axis, {index: this[b].length, isX: "xAxis" === b});
                var n = h ? new f.ColorAxis(this, c) : new w(this, c);
                d[b] = a(d[b] || {});
                d[b].push(c);
                h && (this.isDirtyLegend = !0, this.axes.forEach(function (a) {
                    a.series =
                        []
                }), this.series.forEach(function (a) {
                    a.bindAxes();
                    a.isDirtyData = !0
                }));
                g(r, !0) && this.redraw(l);
                return n
            },
            showLoading: function (a) {
                var b = this, c = b.options, d = b.loadingDiv, e = c.loading, r = function () {
                    d && C(d, {
                        left: b.plotLeft + "px",
                        top: b.plotTop + "px",
                        width: b.plotWidth + "px",
                        height: b.plotHeight + "px"
                    })
                };
                d || (b.loadingDiv = d = L("div", {className: "highcharts-loading highcharts-loading-hidden"}, null, b.container), b.loadingSpan = L("span", {className: "highcharts-loading-inner"}, null, d), H(b, "redraw", r));
                d.className = "highcharts-loading";
                b.loadingSpan.innerHTML = g(a, c.lang.loading, "");
                b.styledMode || (C(d, v(e.style, {zIndex: 10})), C(b.loadingSpan, e.labelStyle), b.loadingShown || (C(d, {
                    opacity: 0,
                    display: ""
                }), M(d, {opacity: e.style.opacity || .5}, {duration: e.showDuration || 0})));
                b.loadingShown = !0;
                r()
            },
            hideLoading: function () {
                var a = this.options, b = this.loadingDiv;
                b && (b.className = "highcharts-loading highcharts-loading-hidden", this.styledMode || M(b, {opacity: 0}, {
                    duration: a.loading.hideDuration || 100,
                    complete: function () {
                        C(b, {display: "none"})
                    }
                }));
                this.loadingShown =
                    !1
            },
            propsRequireDirtyBox: "backgroundColor borderColor borderWidth borderRadius plotBackgroundColor plotBackgroundImage plotBorderColor plotBorderWidth plotShadow shadow".split(" "),
            propsRequireReflow: "margin marginTop marginRight marginBottom marginLeft spacing spacingTop spacingRight spacingBottom spacingLeft".split(" "),
            propsRequireUpdateSeries: "chart.inverted chart.polar chart.ignoreHiddenSeries chart.type colors plotOptions time tooltip".split(" "),
            collectionsWithUpdate: ["xAxis", "yAxis", "zAxis",
                "series"],
            update: function (b, h, t, l) {
                var r = this,
                    w = {credits: "addCredits", title: "setTitle", subtitle: "setSubtitle", caption: "setCaption"}, x,
                    m, z, k = b.isResponsiveOptions, I = [];
                B(r, "update", {options: b});
                k || r.setResponsive(!1, !0);
                b = f.cleanRecursively(b, r.options);
                e(!0, r.userOptions, b);
                if (x = b.chart) {
                    e(!0, r.options.chart, x);
                    "className" in x && r.setClassName(x.className);
                    "reflow" in x && r.setReflow(x.reflow);
                    if ("inverted" in x || "polar" in x || "type" in x) {
                        r.propFromSeries();
                        var D = !0
                    }
                    "alignTicks" in x && (D = !0);
                    d(x, function (a,
                                   b) {
                        -1 !== r.propsRequireUpdateSeries.indexOf("chart." + b) && (m = !0);
                        -1 !== r.propsRequireDirtyBox.indexOf(b) && (r.isDirtyBox = !0);
                        k || -1 === r.propsRequireReflow.indexOf(b) || (z = !0)
                    });
                    !r.styledMode && "style" in x && r.renderer.setStyle(x.style)
                }
                !r.styledMode && b.colors && (this.options.colors = b.colors);
                b.plotOptions && e(!0, this.options.plotOptions, b.plotOptions);
                b.time && this.time === f.time && (this.time = new u(b.time));
                d(b, function (a, b) {
                    if (r[b] && "function" === typeof r[b].update) r[b].update(a, !1); else if ("function" === typeof r[w[b]]) r[w[b]](a);
                    "chart" !== b && -1 !== r.propsRequireUpdateSeries.indexOf(b) && (m = !0)
                });
                this.collectionsWithUpdate.forEach(function (c) {
                    if (b[c]) {
                        if ("series" === c) {
                            var d = [];
                            r[c].forEach(function (a, b) {
                                a.options.isInternal || d.push(g(a.options.index, b))
                            })
                        }
                        a(b[c]).forEach(function (a, b) {
                            (b = F(a.id) && r.get(a.id) || r[c][d ? d[b] : b]) && b.coll === c && (b.update(a, !1), t && (b.touched = !0));
                            !b && t && r.collectionsWithInit[c] && (r.collectionsWithInit[c][0].apply(r, [a].concat(r.collectionsWithInit[c][1] || []).concat([!1])).touched = !0)
                        });
                        t && r[c].forEach(function (a) {
                            a.touched ||
                            a.options.isInternal ? delete a.touched : I.push(a)
                        })
                    }
                });
                I.forEach(function (a) {
                    a.remove && a.remove(!1)
                });
                D && r.axes.forEach(function (a) {
                    a.update({}, !1)
                });
                m && r.getSeriesOrderByLinks().forEach(function (a) {
                    a.chart && a.update({}, !1)
                }, this);
                b.loading && e(!0, r.options.loading, b.loading);
                D = x && x.width;
                x = x && x.height;
                n(x) && (x = c(x, D || r.chartWidth));
                z || p(D) && D !== r.chartWidth || p(x) && x !== r.chartHeight ? r.setSize(D, x, l) : g(h, !0) && r.redraw(l);
                B(r, "afterUpdate", {options: b, redraw: h, animation: l})
            },
            setSubtitle: function (a, b) {
                this.applyDescription("subtitle",
                    a);
                this.layOutTitles(b)
            },
            setCaption: function (a, b) {
                this.applyDescription("caption", a);
                this.layOutTitles(b)
            }
        });
        q.prototype.collectionsWithInit = {
            xAxis: [q.prototype.addAxis, [!0]],
            yAxis: [q.prototype.addAxis, [!1]],
            series: [q.prototype.addSeries]
        };
        v(k.prototype, {
            update: function (a, b, c, d) {
                function h() {
                    e.applyOptions(a);
                    var d = m && e.hasDummyGraphic;
                    d = null === e.y ? !d : d;
                    m && d && (e.graphic = m.destroy(), delete e.hasDummyGraphic);
                    l(a, !0) && (m && m.element && a && a.marker && "undefined" !== typeof a.marker.symbol && (e.graphic = m.destroy()),
                    a && a.dataLabels && e.dataLabel && (e.dataLabel = e.dataLabel.destroy()), e.connector && (e.connector = e.connector.destroy()));
                    r = e.index;
                    t.updateParallelArrays(e, r);
                    n.data[r] = l(n.data[r], !0) || l(a, !0) ? e.options : g(a, n.data[r]);
                    t.isDirty = t.isDirtyData = !0;
                    !t.fixedBox && t.hasCartesianSeries && (f.isDirtyBox = !0);
                    "point" === n.legendType && (f.isDirtyLegend = !0);
                    b && f.redraw(c)
                }

                var e = this, t = e.series, m = e.graphic, r, f = t.chart, n = t.options;
                b = g(b, !0);
                !1 === d ? h() : e.firePointEvent("update", {options: a}, h)
            }, remove: function (a, b) {
                this.series.removePoint(this.series.data.indexOf(this),
                    a, b)
            }
        });
        v(z.prototype, {
            addPoint: function (a, b, c, d, e) {
                var h = this.options, t = this.data, m = this.chart, r = this.xAxis;
                r = r && r.hasNames && r.names;
                var l = h.data, f = this.xData, n;
                b = g(b, !0);
                var w = {series: this};
                this.pointClass.prototype.applyOptions.apply(w, [a]);
                var z = w.x;
                var p = f.length;
                if (this.requireSorting && z < f[p - 1]) for (n = !0; p && f[p - 1] > z;) p--;
                this.updateParallelArrays(w, "splice", p, 0, 0);
                this.updateParallelArrays(w, p);
                r && w.name && (r[z] = w.name);
                l.splice(p, 0, a);
                n && (this.data.splice(p, 0, null), this.processData());
                "point" ===
                h.legendType && this.generatePoints();
                c && (t[0] && t[0].remove ? t[0].remove(!1) : (t.shift(), this.updateParallelArrays(w, "shift"), l.shift()));
                !1 !== e && B(this, "addPoint", {point: w});
                this.isDirtyData = this.isDirty = !0;
                b && m.redraw(d)
            }, removePoint: function (a, c, d) {
                var h = this, e = h.data, t = e[a], r = h.points, m = h.chart, l = function () {
                    r && r.length === e.length && r.splice(a, 1);
                    e.splice(a, 1);
                    h.options.data.splice(a, 1);
                    h.updateParallelArrays(t || {series: h}, "splice", a, 1);
                    t && t.destroy();
                    h.isDirty = !0;
                    h.isDirtyData = !0;
                    c && m.redraw()
                };
                b(d,
                    m);
                c = g(c, !0);
                t ? t.firePointEvent("remove", null, l) : l()
            }, remove: function (a, b, c, d) {
                function h() {
                    e.destroy(d);
                    e.remove = null;
                    t.isDirtyLegend = t.isDirtyBox = !0;
                    t.linkSeries();
                    g(a, !0) && t.redraw(b)
                }

                var e = this, t = e.chart;
                !1 !== c ? B(e, "remove", null, h) : h()
            }, update: function (a, b) {
                a = f.cleanRecursively(a, this.userOptions);
                B(this, "update", {options: a});
                var c = this, d = c.chart, h = c.userOptions, l = c.initialType || c.type,
                    r = a.type || h.type || d.options.chart.type,
                    m = !(this.hasDerivedData || a.dataGrouping || r && r !== this.type || "undefined" !==
                        typeof a.pointStart || a.pointInterval || a.pointIntervalUnit || a.keys), n = D[l].prototype, w,
                    z = ["group", "markerGroup", "dataLabelsGroup", "transformGroup"],
                    p = ["eventOptions", "navigatorSeries", "baseSeries"], k = c.finishedAnimating && {animation: !1},
                    y = {};
                m && (p.push("data", "isDirtyData", "points", "processedXData", "processedYData", "xIncrement", "_hasPointMarkers", "_hasPointLabels", "mapMap", "mapData", "minY", "maxY", "minX", "maxX"), !1 !== a.visible && p.push("area", "graph"), c.parallelArrays.forEach(function (a) {
                    p.push(a + "Data")
                }),
                a.data && (a.dataSorting && v(c.options.dataSorting, a.dataSorting), this.setData(a.data, !1)));
                a = e(h, k, {
                    index: "undefined" === typeof h.index ? c.index : h.index,
                    pointStart: g(h.pointStart, c.xData[0])
                }, !m && {data: c.options.data}, a);
                m && a.data && (a.data = c.options.data);
                p = z.concat(p);
                p.forEach(function (a) {
                    p[a] = c[a];
                    delete c[a]
                });
                c.remove(!1, null, !1, !0);
                for (w in n) c[w] = void 0;
                D[r || l] ? v(c, D[r || l].prototype) : A(17, !0, d, {missingModuleFor: r || l});
                p.forEach(function (a) {
                    c[a] = p[a]
                });
                c.init(d, a);
                if (m && this.points) {
                    var q = c.options;
                    !1 === q.visible ? (y.graphic = 1, y.dataLabel = 1) : c._hasPointLabels || (r = q.marker, n = q.dataLabels, r && (!1 === r.enabled || "symbol" in r) && (y.graphic = 1), n && !1 === n.enabled && (y.dataLabel = 1));
                    this.points.forEach(function (a) {
                        a && a.series && (a.resolveColor(), Object.keys(y).length && a.destroyElements(y), !1 === q.showInLegend && a.legendItem && d.legend.destroyItem(a))
                    }, this)
                }
                a.zIndex !== h.zIndex && z.forEach(function (b) {
                    c[b] && c[b].attr({zIndex: a.zIndex})
                });
                c.initialType = l;
                d.linkSeries();
                B(this, "afterUpdate");
                g(b, !0) && d.redraw(m ?
                    void 0 : !1)
            }, setName: function (a) {
                this.name = this.options.name = this.userOptions.name = a;
                this.chart.isDirtyLegend = !0
            }
        });
        v(w.prototype, {
            update: function (a, b) {
                var c = this.chart, h = a && a.events || {};
                a = e(this.userOptions, a);
                c.options[this.coll].indexOf && (c.options[this.coll][c.options[this.coll].indexOf(this.userOptions)] = a);
                d(c.options[this.coll].events, function (a, b) {
                    "undefined" === typeof h[b] && (h[b] = void 0)
                });
                this.destroy(!0);
                this.init(c, v(a, {events: h}));
                c.isDirtyBox = !0;
                g(b, !0) && c.redraw()
            }, remove: function (a) {
                for (var b =
                    this.chart, c = this.coll, d = this.series, e = d.length; e--;) d[e] && d[e].remove(!1);
                E(b.axes, this);
                E(b[c], this);
                y(b.options[c]) ? b.options[c].splice(this.options.index, 1) : delete b.options[c];
                b[c].forEach(function (a, b) {
                    a.options.index = a.userOptions.index = b
                });
                this.destroy();
                b.isDirtyBox = !0;
                g(a, !0) && b.redraw()
            }, setTitle: function (a, b) {
                this.update({title: a}, b)
            }, setCategories: function (a, b) {
                this.update({categories: a}, b)
            }
        })
    });
    P(u, "parts/AreaSeries.js", [u["parts/Globals.js"], u["parts/Color.js"], u["mixins/legend-symbol.js"],
        u["parts/Utilities.js"]], function (f, k, u, q) {
        var H = k.parse, M = q.objectEach, L = q.pick;
        k = q.seriesType;
        var C = f.Series;
        k("area", "line", {softThreshold: !1, threshold: 0}, {
            singleStacks: !1, getStackPoints: function (f) {
                var k = [], A = [], v = this.xAxis, q = this.yAxis, y = q.stacks[this.stackKey], p = {}, l = this.index,
                    n = q.series, e = n.length, d = L(q.options.reversedStacks, !0) ? 1 : -1, g;
                f = f || this.points;
                if (this.options.stacking) {
                    for (g = 0; g < f.length; g++) f[g].leftNull = f[g].rightNull = void 0, p[f[g].x] = f[g];
                    M(y, function (b, a) {
                        null !== b.total && A.push(a)
                    });
                    A.sort(function (b, a) {
                        return b - a
                    });
                    var c = n.map(function (b) {
                        return b.visible
                    });
                    A.forEach(function (b, a) {
                        var f = 0, n, D;
                        if (p[b] && !p[b].isNull) k.push(p[b]), [-1, 1].forEach(function (f) {
                            var h = 1 === f ? "rightNull" : "leftNull", t = 0, r = y[A[a + f]];
                            if (r) for (g = l; 0 <= g && g < e;) n = r.points[g], n || (g === l ? p[b][h] = !0 : c[g] && (D = y[b].points[g]) && (t -= D[1] - D[0])), g += d;
                            p[b][1 === f ? "rightCliff" : "leftCliff"] = t
                        }); else {
                            for (g = l; 0 <= g && g < e;) {
                                if (n = y[b].points[g]) {
                                    f = n[1];
                                    break
                                }
                                g += d
                            }
                            f = q.translate(f, 0, 1, 0, 1);
                            k.push({
                                isNull: !0, plotX: v.translate(b, 0,
                                    0, 0, 1), x: b, plotY: f, yBottom: f
                            })
                        }
                    })
                }
                return k
            }, getGraphPath: function (f) {
                var k = C.prototype.getGraphPath, A = this.options, v = A.stacking, q = this.yAxis, y, p = [], l = [],
                    n = this.index, e = q.stacks[this.stackKey], d = A.threshold,
                    g = Math.round(q.getThreshold(A.threshold));
                A = L(A.connectNulls, "percent" === v);
                var c = function (b, c, r) {
                    var h = f[b];
                    b = v && e[h.x].points[n];
                    var t = h[r + "Null"] || 0;
                    r = h[r + "Cliff"] || 0;
                    h = !0;
                    if (r || t) {
                        var w = (t ? b[0] : b[1]) + r;
                        var z = b[0] + r;
                        h = !!t
                    } else !v && f[c] && f[c].isNull && (w = z = d);
                    "undefined" !== typeof w && (l.push({
                        plotX: a,
                        plotY: null === w ? g : q.getThreshold(w), isNull: h, isCliff: !0
                    }), p.push({plotX: a, plotY: null === z ? g : q.getThreshold(z), doCurve: !1}))
                };
                f = f || this.points;
                v && (f = this.getStackPoints(f));
                for (y = 0; y < f.length; y++) {
                    v || (f[y].leftCliff = f[y].rightCliff = f[y].leftNull = f[y].rightNull = void 0);
                    var b = f[y].isNull;
                    var a = L(f[y].rectPlotX, f[y].plotX);
                    var w = L(f[y].yBottom, g);
                    if (!b || A) A || c(y, y - 1, "left"), b && !v && A || (l.push(f[y]), p.push({
                        x: y,
                        plotX: a,
                        plotY: w
                    })), A || c(y, y + 1, "right")
                }
                y = k.call(this, l, !0, !0);
                p.reversed = !0;
                b = k.call(this, p, !0,
                    !0);
                b.length && (b[0] = "L");
                b = y.concat(b);
                k = k.call(this, l, !1, A);
                b.xMap = y.xMap;
                this.areaPath = b;
                return k
            }, drawGraph: function () {
                this.areaPath = [];
                C.prototype.drawGraph.apply(this);
                var f = this, k = this.areaPath, A = this.options,
                    v = [["area", "highcharts-area", this.color, A.fillColor]];
                this.zones.forEach(function (k, y) {
                    v.push(["zone-area-" + y, "highcharts-area highcharts-zone-area-" + y + " " + k.className, k.color || f.color, k.fillColor || A.fillColor])
                });
                v.forEach(function (v) {
                    var y = v[0], p = f[y], l = p ? "animate" : "attr", n = {};
                    p ? (p.endX =
                        f.preventGraphAnimation ? null : k.xMap, p.animate({d: k})) : (n.zIndex = 0, p = f[y] = f.chart.renderer.path(k).addClass(v[1]).add(f.group), p.isArea = !0);
                    f.chart.styledMode || (n.fill = L(v[3], H(v[2]).setOpacity(L(A.fillOpacity, .75)).get()));
                    p[l](n);
                    p.startX = k.xMap;
                    p.shiftUnit = A.step ? 2 : 1
                })
            }, drawLegendSymbol: u.drawRectangle
        });
        ""
    });
    P(u, "parts/SplineSeries.js", [u["parts/Utilities.js"]], function (f) {
        var k = f.pick;
        f = f.seriesType;
        f("spline", "line", {}, {
            getPointSpline: function (f, q, u) {
                var H = q.plotX, G = q.plotY, C = f[u - 1];
                u = f[u + 1];
                if (C && !C.isNull && !1 !== C.doCurve && !q.isCliff && u && !u.isNull && !1 !== u.doCurve && !q.isCliff) {
                    f = C.plotY;
                    var F = u.plotX;
                    u = u.plotY;
                    var E = 0;
                    var A = (1.5 * H + C.plotX) / 2.5;
                    var v = (1.5 * G + f) / 2.5;
                    F = (1.5 * H + F) / 2.5;
                    var B = (1.5 * G + u) / 2.5;
                    F !== A && (E = (B - v) * (F - H) / (F - A) + G - B);
                    v += E;
                    B += E;
                    v > f && v > G ? (v = Math.max(f, G), B = 2 * G - v) : v < f && v < G && (v = Math.min(f, G), B = 2 * G - v);
                    B > u && B > G ? (B = Math.max(u, G), v = 2 * G - B) : B < u && B < G && (B = Math.min(u, G), v = 2 * G - B);
                    q.rightContX = F;
                    q.rightContY = B
                }
                q = ["C", k(C.rightContX, C.plotX), k(C.rightContY, C.plotY), k(A, H), k(v, G), H, G];
                C.rightContX =
                    C.rightContY = null;
                return q
            }
        });
        ""
    });
    P(u, "parts/AreaSplineSeries.js", [u["parts/Globals.js"], u["mixins/legend-symbol.js"], u["parts/Utilities.js"]], function (f, k, u) {
        u = u.seriesType;
        var q = f.seriesTypes.area.prototype;
        u("areaspline", "spline", f.defaultPlotOptions.area, {
            getStackPoints: q.getStackPoints,
            getGraphPath: q.getGraphPath,
            drawGraph: q.drawGraph,
            drawLegendSymbol: k.drawRectangle
        });
        ""
    });
    P(u, "parts/ColumnSeries.js", [u["parts/Globals.js"], u["parts/Color.js"], u["mixins/legend-symbol.js"], u["parts/Utilities.js"]],
        function (f, k, u, q) {
            "";
            var H = k.parse, M = q.animObject, L = q.clamp, C = q.defined, F = q.extend, E = q.isNumber, A = q.merge,
                v = q.pick;
            k = q.seriesType;
            var B = f.Series;
            k("column", "line", {
                borderRadius: 0,
                crisp: !0,
                groupPadding: .2,
                marker: null,
                pointPadding: .1,
                minPointLength: 0,
                cropThreshold: 50,
                pointRange: null,
                states: {hover: {halo: !1, brightness: .1}, select: {color: "#cccccc", borderColor: "#000000"}},
                dataLabels: {align: null, verticalAlign: null, y: null},
                softThreshold: !1,
                startFromThreshold: !0,
                stickyTracking: !1,
                tooltip: {distance: 6},
                threshold: 0,
                borderColor: "#ffffff"
            }, {
                cropShoulder: 0,
                directTouch: !0,
                trackerGroups: ["group", "dataLabelsGroup"],
                negStacks: !0,
                init: function () {
                    B.prototype.init.apply(this, arguments);
                    var f = this, p = f.chart;
                    p.hasRendered && p.series.forEach(function (l) {
                        l.type === f.type && (l.isDirty = !0)
                    })
                },
                getColumnMetrics: function () {
                    var f = this, p = f.options, l = f.xAxis, n = f.yAxis, e = l.options.reversedStacks;
                    e = l.reversed && !e || !l.reversed && e;
                    var d, g = {}, c = 0;
                    !1 === p.grouping ? c = 1 : f.chart.series.forEach(function (a) {
                        var b = a.yAxis, e = a.options;
                        if (a.type ===
                            f.type && (a.visible || !f.chart.options.chart.ignoreHiddenSeries) && n.len === b.len && n.pos === b.pos) {
                            if (e.stacking) {
                                d = a.stackKey;
                                "undefined" === typeof g[d] && (g[d] = c++);
                                var h = g[d]
                            } else !1 !== e.grouping && (h = c++);
                            a.columnIndex = h
                        }
                    });
                    var b = Math.min(Math.abs(l.transA) * (l.ordinalSlope || p.pointRange || l.closestPointRange || l.tickInterval || 1), l.len),
                        a = b * p.groupPadding, w = (b - 2 * a) / (c || 1);
                    p = Math.min(p.maxPointWidth || l.len, v(p.pointWidth, w * (1 - 2 * p.pointPadding)));
                    f.columnMetrics = {
                        width: p, offset: (w - p) / 2 + (a + ((f.columnIndex ||
                            0) + (e ? 1 : 0)) * w - b / 2) * (e ? -1 : 1)
                    };
                    return f.columnMetrics
                },
                crispCol: function (f, p, l, n) {
                    var e = this.chart, d = this.borderWidth, g = -(d % 2 ? .5 : 0);
                    d = d % 2 ? .5 : 1;
                    e.inverted && e.renderer.isVML && (d += 1);
                    this.options.crisp && (l = Math.round(f + l) + g, f = Math.round(f) + g, l -= f);
                    n = Math.round(p + n) + d;
                    g = .5 >= Math.abs(p) && .5 < n;
                    p = Math.round(p) + d;
                    n -= p;
                    g && n && (--p, n += 1);
                    return {x: f, y: p, width: l, height: n}
                },
                translate: function () {
                    var f = this, p = f.chart, l = f.options, n = f.dense = 2 > f.closestPointRange * f.xAxis.transA;
                    n = f.borderWidth = v(l.borderWidth, n ? 0 : 1);
                    var e = f.xAxis, d = f.yAxis, g = l.threshold, c = f.translatedThreshold = d.getThreshold(g),
                        b = v(l.minPointLength, 5), a = f.getColumnMetrics(), w = a.width,
                        z = f.barW = Math.max(w, 1 + 2 * n), k = f.pointXOffset = a.offset, r = f.dataMin,
                        h = f.dataMax;
                    p.inverted && (c -= .5);
                    l.pointPadding && (z = Math.ceil(z));
                    B.prototype.translate.apply(f);
                    f.points.forEach(function (a) {
                        var t = v(a.yBottom, c), l = 999 + Math.abs(t), n = w, x = a.plotX;
                        l = L(a.plotY, -l, d.len + l);
                        var m = a.plotX + k, D = z, A = Math.min(l, t), q = Math.max(l, t) - A;
                        if (b && Math.abs(q) < b) {
                            q = b;
                            var y = !d.reversed &&
                                !a.negative || d.reversed && a.negative;
                            a.y === g && f.dataMax <= g && d.min < g && r !== h && (y = !y);
                            A = Math.abs(A - c) > b ? t - b : c - (y ? b : 0)
                        }
                        C(a.options.pointWidth) && (n = D = Math.ceil(a.options.pointWidth), m -= Math.round((n - w) / 2));
                        a.barX = m;
                        a.pointWidth = n;
                        a.tooltipPos = p.inverted ? [d.len + d.pos - p.plotLeft - l, e.len + e.pos - p.plotTop - (x || 0) - k - D / 2, q] : [m + D / 2, l + d.pos - p.plotTop, q];
                        a.shapeType = f.pointClass.prototype.shapeType || "rect";
                        a.shapeArgs = f.crispCol.apply(f, a.isNull ? [m, c, D, 0] : [m, A, D, q])
                    })
                },
                getSymbol: f.noop,
                drawLegendSymbol: u.drawRectangle,
                drawGraph: function () {
                    this.group[this.dense ? "addClass" : "removeClass"]("highcharts-dense-data")
                },
                pointAttribs: function (f, p) {
                    var l = this.options, n = this.pointAttrToOptions || {};
                    var e = n.stroke || "borderColor";
                    var d = n["stroke-width"] || "borderWidth", g = f && f.color || this.color,
                        c = f && f[e] || l[e] || this.color || g, b = f && f[d] || l[d] || this[d] || 0;
                    n = f && f.options.dashStyle || l.dashStyle;
                    var a = v(f && f.opacity, l.opacity, 1);
                    if (f && this.zones.length) {
                        var w = f.getZone();
                        g = f.options.color || w && (w.color || f.nonZonedColor) || this.color;
                        w &&
                        (c = w.borderColor || c, n = w.dashStyle || n, b = w.borderWidth || b)
                    }
                    p && f && (f = A(l.states[p], f.options.states && f.options.states[p] || {}), p = f.brightness, g = f.color || "undefined" !== typeof p && H(g).brighten(f.brightness).get() || g, c = f[e] || c, b = f[d] || b, n = f.dashStyle || n, a = v(f.opacity, a));
                    e = {fill: g, stroke: c, "stroke-width": b, opacity: a};
                    n && (e.dashstyle = n);
                    return e
                },
                drawPoints: function () {
                    var f = this, p = this.chart, l = f.options, n = p.renderer, e = l.animationLimit || 250, d;
                    f.points.forEach(function (g) {
                        var c = g.graphic, b = !!c, a = c && p.pointCount <
                        e ? "animate" : "attr";
                        if (E(g.plotY) && null !== g.y) {
                            d = g.shapeArgs;
                            c && g.hasNewShapeType() && (c = c.destroy());
                            f.enabledDataSorting && (g.startXPos = f.xAxis.reversed ? -(d ? d.width : 0) : f.xAxis.width);
                            c || (g.graphic = c = n[g.shapeType](d).add(g.group || f.group)) && f.enabledDataSorting && p.hasRendered && p.pointCount < e && (c.attr({x: g.startXPos}), b = !0, a = "animate");
                            if (c && b) c[a](A(d));
                            if (l.borderRadius) c[a]({r: l.borderRadius});
                            p.styledMode || c[a](f.pointAttribs(g, g.selected && "select")).shadow(!1 !== g.allowShadow && l.shadow, null, l.stacking &&
                                !l.borderRadius);
                            c.addClass(g.getClassName(), !0)
                        } else c && (g.graphic = c.destroy())
                    })
                },
                animate: function (f) {
                    var p = this, l = this.yAxis, n = p.options, e = this.chart.inverted, d = {},
                        g = e ? "translateX" : "translateY";
                    if (f) d.scaleY = .001, f = L(l.toPixels(n.threshold), l.pos, l.pos + l.len), e ? d.translateX = f - l.len : d.translateY = f, p.clipBox && p.setClip(), p.group.attr(d); else {
                        var c = p.group.attr(g);
                        p.group.animate({scaleY: 1}, F(M(p.options.animation), {
                            step: function (b, a) {
                                p.group && (d[g] = c + a.pos * (l.pos - c), p.group.attr(d))
                            }
                        }))
                    }
                },
                remove: function () {
                    var f =
                        this, p = f.chart;
                    p.hasRendered && p.series.forEach(function (l) {
                        l.type === f.type && (l.isDirty = !0)
                    });
                    B.prototype.remove.apply(f, arguments)
                }
            });
            ""
        });
    P(u, "parts/BarSeries.js", [u["parts/Utilities.js"]], function (f) {
        f = f.seriesType;
        f("bar", "column", null, {inverted: !0});
        ""
    });
    P(u, "parts/ScatterSeries.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent;
        k = k.seriesType;
        var q = f.Series;
        k("scatter", "line", {
            lineWidth: 0, findNearestPointBy: "xy", jitter: {x: 0, y: 0}, marker: {enabled: !0}, tooltip: {
                headerFormat: '<span style="color:{point.color}">\u25cf</span> <span style="font-size: 10px"> {series.name}</span><br/>',
                pointFormat: "x: <b>{point.x}</b><br/>y: <b>{point.y}</b><br/>"
            }
        }, {
            sorted: !1,
            requireSorting: !1,
            noSharedTooltip: !0,
            trackerGroups: ["group", "markerGroup", "dataLabelsGroup"],
            takeOrdinalPosition: !1,
            drawGraph: function () {
                this.options.lineWidth && q.prototype.drawGraph.call(this)
            },
            applyJitter: function () {
                var f = this, k = this.options.jitter, q = this.points.length;
                k && this.points.forEach(function (C, u) {
                    ["x", "y"].forEach(function (E, A) {
                        var v = "plot" + E.toUpperCase();
                        if (k[E] && !C.isNull) {
                            var B = f[E + "Axis"];
                            var y = k[E] * B.transA;
                            if (B && !B.isLog) {
                                var p = Math.max(0, C[v] - y);
                                B = Math.min(B.len, C[v] + y);
                                A = 1E4 * Math.sin(u + A * q);
                                C[v] = p + (B - p) * (A - Math.floor(A));
                                "x" === E && (C.clientX = C.plotX)
                            }
                        }
                    })
                })
            }
        });
        u(q, "afterTranslate", function () {
            this.applyJitter && this.applyJitter()
        });
        ""
    });
    P(u, "mixins/centered-series.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.isNumber, q = k.pick, G = k.relativeLength, M = f.deg2rad;
        f.CenteredSeriesMixin = {
            getCenter: function () {
                var f = this.options, k = this.chart, u = 2 * (f.slicedOffset || 0), E = k.plotWidth - 2 * u,
                    A = k.plotHeight - 2 * u, v = f.center, B = Math.min(E, A), y = f.size, p = f.innerSize || 0;
                "string" === typeof y && (y = parseFloat(y));
                "string" === typeof p && (p = parseFloat(p));
                f = [q(v[0], "50%"), q(v[1], "50%"), q(y && 0 > y ? void 0 : f.size, "100%"), q(p && 0 > p ? void 0 : f.innerSize || 0, "0%")];
                k.angular && (f[3] = 0);
                for (v = 0; 4 > v; ++v) y = f[v], k = 2 > v || 2 === v && /%$/.test(y), f[v] = G(y, [E, A, B, f[2]][v]) + (k ? u : 0);
                f[3] > f[2] && (f[3] = f[2]);
                return f
            }, getStartAndEndRadians: function (f, k) {
                f = u(f) ? f : 0;
                k = u(k) && k > f && 360 > k - f ? k : f + 360;
                return {start: M * (f + -90), end: M * (k + -90)}
            }
        }
    });
    P(u, "parts/PieSeries.js", [u["parts/Globals.js"], u["mixins/legend-symbol.js"], u["parts/Point.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        var H = q.addEvent, M = q.clamp, L = q.defined, C = q.fireEvent, F = q.isNumber, E = q.merge, A = q.pick,
            v = q.relativeLength, B = q.seriesType, y = q.setAnimation;
        q = f.CenteredSeriesMixin;
        var p = q.getStartAndEndRadians, l = f.noop, n = f.Series;
        B("pie", "line", {
            center: [null, null],
            clip: !1,
            colorByPoint: !0,
            dataLabels: {
                allowOverlap: !0, connectorPadding: 5, connectorShape: "fixedOffset", crookDistance: "70%",
                distance: 30, enabled: !0, formatter: function () {
                    return this.point.isNull ? void 0 : this.point.name
                }, softConnector: !0, x: 0
            },
            fillColor: void 0,
            ignoreHiddenPoint: !0,
            inactiveOtherPoints: !0,
            legendType: "point",
            marker: null,
            size: null,
            showInLegend: !1,
            slicedOffset: 10,
            stickyTracking: !1,
            tooltip: {followPointer: !0},
            borderColor: "#ffffff",
            borderWidth: 1,
            lineWidth: void 0,
            states: {hover: {brightness: .1}}
        }, {
            isCartesian: !1,
            requireSorting: !1,
            directTouch: !0,
            noSharedTooltip: !0,
            trackerGroups: ["group", "dataLabelsGroup"],
            axisTypes: [],
            pointAttribs: f.seriesTypes.column.prototype.pointAttribs,
            animate: function (e) {
                var d = this, g = d.points, c = d.startAngleRad;
                e || g.forEach(function (b) {
                    var a = b.graphic, e = b.shapeArgs;
                    a && e && (a.attr({
                        r: A(b.startR, d.center && d.center[3] / 2),
                        start: c,
                        end: c
                    }), a.animate({r: e.r, start: e.start, end: e.end}, d.options.animation))
                })
            },
            hasData: function () {
                return !!this.processedXData.length
            },
            updateTotals: function () {
                var e, d = 0, g = this.points, c = g.length, b = this.options.ignoreHiddenPoint;
                for (e = 0; e < c; e++) {
                    var a = g[e];
                    d += b && !a.visible ? 0 :
                        a.isNull ? 0 : a.y
                }
                this.total = d;
                for (e = 0; e < c; e++) a = g[e], a.percentage = 0 < d && (a.visible || !b) ? a.y / d * 100 : 0, a.total = d
            },
            generatePoints: function () {
                n.prototype.generatePoints.call(this);
                this.updateTotals()
            },
            getX: function (e, d, g) {
                var c = this.center, b = this.radii ? this.radii[g.index] : c[2] / 2;
                e = Math.asin(M((e - c[1]) / (b + g.labelDistance), -1, 1));
                return c[0] + (d ? -1 : 1) * Math.cos(e) * (b + g.labelDistance) + (0 < g.labelDistance ? (d ? -1 : 1) * this.options.dataLabels.padding : 0)
            },
            translate: function (e) {
                this.generatePoints();
                var d = 0, g = this.options,
                    c = g.slicedOffset, b = c + (g.borderWidth || 0), a = p(g.startAngle, g.endAngle),
                    f = this.startAngleRad = a.start;
                a = (this.endAngleRad = a.end) - f;
                var l = this.points, n = g.dataLabels.distance;
                g = g.ignoreHiddenPoint;
                var r, h = l.length;
                e || (this.center = e = this.getCenter());
                for (r = 0; r < h; r++) {
                    var t = l[r];
                    var k = f + d * a;
                    if (!g || t.visible) d += t.percentage / 100;
                    var J = f + d * a;
                    t.shapeType = "arc";
                    t.shapeArgs = {
                        x: e[0],
                        y: e[1],
                        r: e[2] / 2,
                        innerR: e[3] / 2,
                        start: Math.round(1E3 * k) / 1E3,
                        end: Math.round(1E3 * J) / 1E3
                    };
                    t.labelDistance = A(t.options.dataLabels && t.options.dataLabels.distance,
                        n);
                    t.labelDistance = v(t.labelDistance, t.shapeArgs.r);
                    this.maxLabelDistance = Math.max(this.maxLabelDistance || 0, t.labelDistance);
                    J = (J + k) / 2;
                    J > 1.5 * Math.PI ? J -= 2 * Math.PI : J < -Math.PI / 2 && (J += 2 * Math.PI);
                    t.slicedTranslation = {
                        translateX: Math.round(Math.cos(J) * c),
                        translateY: Math.round(Math.sin(J) * c)
                    };
                    var N = Math.cos(J) * e[2] / 2;
                    var x = Math.sin(J) * e[2] / 2;
                    t.tooltipPos = [e[0] + .7 * N, e[1] + .7 * x];
                    t.half = J < -Math.PI / 2 || J > Math.PI / 2 ? 1 : 0;
                    t.angle = J;
                    k = Math.min(b, t.labelDistance / 5);
                    t.labelPosition = {
                        natural: {
                            x: e[0] + N + Math.cos(J) * t.labelDistance,
                            y: e[1] + x + Math.sin(J) * t.labelDistance
                        },
                        "final": {},
                        alignment: 0 > t.labelDistance ? "center" : t.half ? "right" : "left",
                        connectorPosition: {
                            breakAt: {x: e[0] + N + Math.cos(J) * k, y: e[1] + x + Math.sin(J) * k},
                            touchingSliceAt: {x: e[0] + N, y: e[1] + x}
                        }
                    }
                }
                C(this, "afterTranslate")
            },
            drawEmpty: function () {
                var e = this.options;
                if (0 === this.total) {
                    var d = this.center[0];
                    var g = this.center[1];
                    this.graph || (this.graph = this.chart.renderer.circle(d, g, 0).addClass("highcharts-graph").add(this.group));
                    this.graph.animate({
                        "stroke-width": e.borderWidth,
                        cx: d, cy: g, r: this.center[2] / 2, fill: e.fillColor || "none", stroke: e.color || "#cccccc"
                    }, this.options.animation)
                } else this.graph && (this.graph = this.graph.destroy())
            },
            redrawPoints: function () {
                var e = this, d = e.chart, g = d.renderer, c, b, a, f, l = e.options.shadow;
                this.drawEmpty();
                !l || e.shadowGroup || d.styledMode || (e.shadowGroup = g.g("shadow").attr({zIndex: -1}).add(e.group));
                e.points.forEach(function (n) {
                    var r = {};
                    b = n.graphic;
                    if (!n.isNull && b) {
                        f = n.shapeArgs;
                        c = n.getTranslate();
                        if (!d.styledMode) {
                            var h = n.shadowGroup;
                            l && !h && (h =
                                n.shadowGroup = g.g("shadow").add(e.shadowGroup));
                            h && h.attr(c);
                            a = e.pointAttribs(n, n.selected && "select")
                        }
                        n.delayedRendering ? (b.setRadialReference(e.center).attr(f).attr(c), d.styledMode || b.attr(a).attr({"stroke-linejoin": "round"}).shadow(l, h), n.delayedRendering = !1) : (b.setRadialReference(e.center), d.styledMode || E(!0, r, a), E(!0, r, f, c), b.animate(r));
                        b.attr({visibility: n.visible ? "inherit" : "hidden"});
                        b.addClass(n.getClassName())
                    } else b && (n.graphic = b.destroy())
                })
            },
            drawPoints: function () {
                var e = this.chart.renderer;
                this.points.forEach(function (d) {
                    d.graphic && d.hasNewShapeType() && (d.graphic = d.graphic.destroy());
                    d.graphic || (d.graphic = e[d.shapeType](d.shapeArgs).add(d.series.group), d.delayedRendering = !0)
                })
            },
            searchPoint: l,
            sortByAngle: function (e, d) {
                e.sort(function (e, c) {
                    return "undefined" !== typeof e.angle && (c.angle - e.angle) * d
                })
            },
            drawLegendSymbol: k.drawRectangle,
            getCenter: q.getCenter,
            getSymbol: l,
            drawGraph: null
        }, {
            init: function () {
                u.prototype.init.apply(this, arguments);
                var e = this;
                e.name = A(e.name, "Slice");
                var d = function (d) {
                    e.slice("select" ===
                        d.type)
                };
                H(e, "select", d);
                H(e, "unselect", d);
                return e
            }, isValid: function () {
                return F(this.y) && 0 <= this.y
            }, setVisible: function (e, d) {
                var g = this, c = g.series, b = c.chart, a = c.options.ignoreHiddenPoint;
                d = A(d, a);
                e !== g.visible && (g.visible = g.options.visible = e = "undefined" === typeof e ? !g.visible : e, c.options.data[c.data.indexOf(g)] = g.options, ["graphic", "dataLabel", "connector", "shadowGroup"].forEach(function (a) {
                    if (g[a]) g[a][e ? "show" : "hide"](!0)
                }), g.legendItem && b.legend.colorizeItem(g, e), e || "hover" !== g.state || g.setState(""),
                a && (c.isDirty = !0), d && b.redraw())
            }, slice: function (e, d, g) {
                var c = this.series;
                y(g, c.chart);
                A(d, !0);
                this.sliced = this.options.sliced = L(e) ? e : !this.sliced;
                c.options.data[c.data.indexOf(this)] = this.options;
                this.graphic.animate(this.getTranslate());
                this.shadowGroup && this.shadowGroup.animate(this.getTranslate())
            }, getTranslate: function () {
                return this.sliced ? this.slicedTranslation : {translateX: 0, translateY: 0}
            }, haloPath: function (e) {
                var d = this.shapeArgs;
                return this.sliced || !this.visible ? [] : this.series.chart.renderer.symbols.arc(d.x,
                    d.y, d.r + e, d.r + e, {innerR: d.r - 1, start: d.start, end: d.end})
            }, connectorShapes: {
                fixedOffset: function (e, d, g) {
                    var c = d.breakAt;
                    d = d.touchingSliceAt;
                    return ["M", e.x, e.y].concat(g.softConnector ? ["C", e.x + ("left" === e.alignment ? -5 : 5), e.y, 2 * c.x - d.x, 2 * c.y - d.y, c.x, c.y] : ["L", c.x, c.y]).concat(["L", d.x, d.y])
                }, straight: function (e, d) {
                    d = d.touchingSliceAt;
                    return ["M", e.x, e.y, "L", d.x, d.y]
                }, crookedLine: function (e, d, g) {
                    d = d.touchingSliceAt;
                    var c = this.series, b = c.center[0], a = c.chart.plotWidth, f = c.chart.plotLeft;
                    c = e.alignment;
                    var l =
                        this.shapeArgs.r;
                    g = v(g.crookDistance, 1);
                    g = "left" === c ? b + l + (a + f - b - l) * (1 - g) : f + (b - l) * g;
                    b = ["L", g, e.y];
                    if ("left" === c ? g > e.x || g < d.x : g < e.x || g > d.x) b = [];
                    return ["M", e.x, e.y].concat(b).concat(["L", d.x, d.y])
                }
            }, getConnectorPath: function () {
                var e = this.labelPosition, d = this.series.options.dataLabels, g = d.connectorShape,
                    c = this.connectorShapes;
                c[g] && (g = c[g]);
                return g.call(this, {x: e.final.x, y: e.final.y, alignment: e.alignment}, e.connectorPosition, d)
            }
        });
        ""
    });
    P(u, "parts/DataLabels.js", [u["parts/Globals.js"], u["parts/Utilities.js"]],
        function (f, k) {
            var u = k.animObject, q = k.arrayMax, G = k.clamp, M = k.defined, L = k.extend, C = k.format, F = k.isArray,
                E = k.merge, A = k.objectEach, v = k.pick, B = k.relativeLength, y = k.splat, p = k.stableSort;
            k = f.noop;
            var l = f.Series, n = f.seriesTypes;
            f.distribute = function (e, d, g) {
                function c(a, b) {
                    return a.target - b.target
                }

                var b, a = !0, l = e, n = [];
                var k = 0;
                var r = l.reducedLen || d;
                for (b = e.length; b--;) k += e[b].size;
                if (k > r) {
                    p(e, function (a, b) {
                        return (b.rank || 0) - (a.rank || 0)
                    });
                    for (k = b = 0; k <= r;) k += e[b].size, b++;
                    n = e.splice(b - 1, e.length)
                }
                p(e, c);
                for (e =
                         e.map(function (a) {
                             return {size: a.size, targets: [a.target], align: v(a.align, .5)}
                         }); a;) {
                    for (b = e.length; b--;) a = e[b], k = (Math.min.apply(0, a.targets) + Math.max.apply(0, a.targets)) / 2, a.pos = G(k - a.size * a.align, 0, d - a.size);
                    b = e.length;
                    for (a = !1; b--;) 0 < b && e[b - 1].pos + e[b - 1].size > e[b].pos && (e[b - 1].size += e[b].size, e[b - 1].targets = e[b - 1].targets.concat(e[b].targets), e[b - 1].align = .5, e[b - 1].pos + e[b - 1].size > d && (e[b - 1].pos = d - e[b - 1].size), e.splice(b, 1), a = !0)
                }
                l.push.apply(l, n);
                b = 0;
                e.some(function (a) {
                    var c = 0;
                    if (a.targets.some(function () {
                        l[b].pos =
                            a.pos + c;
                        if ("undefined" !== typeof g && Math.abs(l[b].pos - l[b].target) > g) return l.slice(0, b + 1).forEach(function (a) {
                            delete a.pos
                        }), l.reducedLen = (l.reducedLen || d) - .1 * d, l.reducedLen > .1 * d && f.distribute(l, d, g), !0;
                        c += l[b].size;
                        b++
                    })) return !0
                });
                p(l, c)
            };
            l.prototype.drawDataLabels = function () {
                function e(a, b) {
                    var c = b.filter;
                    return c ? (b = c.operator, a = a[c.property], c = c.value, ">" === b && a > c || "<" === b && a < c || ">=" === b && a >= c || "<=" === b && a <= c || "==" === b && a == c || "===" === b && a === c ? !0 : !1) : !0
                }

                function d(a, b) {
                    var c = [], d;
                    if (F(a) && !F(b)) c =
                        a.map(function (a) {
                            return E(a, b)
                        }); else if (F(b) && !F(a)) c = b.map(function (b) {
                        return E(a, b)
                    }); else if (F(a) || F(b)) for (d = Math.max(a.length, b.length); d--;) c[d] = E(a[d], b[d]); else c = E(a, b);
                    return c
                }

                var g = this, c = g.chart, b = g.options, a = b.dataLabels, l = g.points, n, k = g.hasRendered || 0,
                    r = u(b.animation).duration, h = Math.min(r, 200), t = !c.renderer.forExport && v(a.defer, 0 < h),
                    p = c.renderer;
                a = d(d(c.options.plotOptions && c.options.plotOptions.series && c.options.plotOptions.series.dataLabels, c.options.plotOptions && c.options.plotOptions[g.type] &&
                    c.options.plotOptions[g.type].dataLabels), a);
                f.fireEvent(this, "drawDataLabels");
                if (F(a) || a.enabled || g._hasPointLabels) {
                    var J = g.plotGroup("dataLabelsGroup", "data-labels", t && !k ? "hidden" : "inherit", a.zIndex || 6);
                    t && (J.attr({opacity: +k}), k || setTimeout(function () {
                        var a = g.dataLabelsGroup;
                        a && (g.visible && J.show(!0), a[b.animation ? "animate" : "attr"]({opacity: 1}, {duration: h}))
                    }, r - h));
                    l.forEach(function (h) {
                        n = y(d(a, h.dlOptions || h.options && h.options.dataLabels));
                        n.forEach(function (a, d) {
                            var m = a.enabled && (!h.isNull ||
                                h.dataLabelOnNull) && e(h, a), f = h.dataLabels ? h.dataLabels[d] : h.dataLabel,
                                l = h.connectors ? h.connectors[d] : h.connector, t = v(a.distance, h.labelDistance),
                                r = !f;
                            if (m) {
                                var n = h.getLabelConfig();
                                var w = v(a[h.formatPrefix + "Format"], a.format);
                                n = M(w) ? C(w, n, c) : (a[h.formatPrefix + "Formatter"] || a.formatter).call(n, a);
                                w = a.style;
                                var k = a.rotation;
                                c.styledMode || (w.color = v(a.color, w.color, g.color, "#000000"), "contrast" === w.color ? (h.contrastColor = p.getContrast(h.color || g.color), w.color = !M(t) && a.inside || 0 > t || b.stacking ? h.contrastColor :
                                    "#000000") : delete h.contrastColor, b.cursor && (w.cursor = b.cursor));
                                var z = {r: a.borderRadius || 0, rotation: k, padding: a.padding, zIndex: 1};
                                c.styledMode || (z.fill = a.backgroundColor, z.stroke = a.borderColor, z["stroke-width"] = a.borderWidth);
                                A(z, function (a, b) {
                                    "undefined" === typeof a && delete z[b]
                                })
                            }
                            !f || m && M(n) ? m && M(n) && (f ? z.text = n : (h.dataLabels = h.dataLabels || [], f = h.dataLabels[d] = k ? p.text(n, 0, -9999, a.useHTML).addClass("highcharts-data-label") : p.label(n, 0, -9999, a.shape, null, null, a.useHTML, null, "data-label"), d || (h.dataLabel =
                                f), f.addClass(" highcharts-data-label-color-" + h.colorIndex + " " + (a.className || "") + (a.useHTML ? " highcharts-tracker" : ""))), f.options = a, f.attr(z), c.styledMode || f.css(w).shadow(a.shadow), f.added || f.add(J), a.textPath && !a.useHTML && (f.setTextPath(h.getDataLabelPath && h.getDataLabelPath(f) || h.graphic, a.textPath), h.dataLabelPath && !a.textPath.enabled && (h.dataLabelPath = h.dataLabelPath.destroy())), g.alignDataLabel(h, f, a, null, r)) : (h.dataLabel = h.dataLabel && h.dataLabel.destroy(), h.dataLabels && (1 === h.dataLabels.length ?
                                delete h.dataLabels : delete h.dataLabels[d]), d || delete h.dataLabel, l && (h.connector = h.connector.destroy(), h.connectors && (1 === h.connectors.length ? delete h.connectors : delete h.connectors[d])))
                        })
                    })
                }
                f.fireEvent(this, "afterDrawDataLabels")
            };
            l.prototype.alignDataLabel = function (e, d, g, c, b) {
                var a = this, f = this.chart, l = this.isCartesian && f.inverted, n = this.enabledDataSorting,
                    r = v(e.dlBox && e.dlBox.centerX, e.plotX, -9999), h = v(e.plotY, -9999), t = d.getBBox(),
                    k = g.rotation, p = g.align, q = f.isInsidePlot(r, Math.round(h), l), x = "justify" ===
                    v(g.overflow, n ? "none" : "justify"),
                    m = this.visible && !1 !== e.visible && (e.series.forceDL || n && !x || q || g.inside && c && f.isInsidePlot(r, l ? c.x + 1 : c.y + c.height - 1, l));
                var A = function (c) {
                    n && a.xAxis && !x && a.setDataLabelStartPos(e, d, b, q, c)
                };
                if (m) {
                    var y = f.renderer.fontMetrics(f.styledMode ? void 0 : g.style.fontSize, d).b;
                    c = L({
                        x: l ? this.yAxis.len - h : r,
                        y: Math.round(l ? this.xAxis.len - r : h),
                        width: 0,
                        height: 0
                    }, c);
                    L(g, {width: t.width, height: t.height});
                    k ? (x = !1, r = f.renderer.rotCorr(y, k), r = {
                        x: c.x + g.x + c.width / 2 + r.x, y: c.y + g.y + {
                            top: 0, middle: .5,
                            bottom: 1
                        }[g.verticalAlign] * c.height
                    }, A(r), d[b ? "attr" : "animate"](r).attr({align: p}), A = (k + 720) % 360, A = 180 < A && 360 > A, "left" === p ? r.y -= A ? t.height : 0 : "center" === p ? (r.x -= t.width / 2, r.y -= t.height / 2) : "right" === p && (r.x -= t.width, r.y -= A ? 0 : t.height), d.placed = !0, d.alignAttr = r) : (A(c), d.align(g, null, c), r = d.alignAttr);
                    x && 0 <= c.height ? this.justifyDataLabel(d, g, r, t, c, b) : v(g.crop, !0) && (m = f.isInsidePlot(r.x, r.y) && f.isInsidePlot(r.x + t.width, r.y + t.height));
                    if (g.shape && !k) d[b ? "attr" : "animate"]({
                        anchorX: l ? f.plotWidth - e.plotY :
                            e.plotX, anchorY: l ? f.plotHeight - e.plotX : e.plotY
                    })
                }
                b && n && (d.placed = !1);
                m || n && !x || (d.hide(!0), d.placed = !1)
            };
            l.prototype.setDataLabelStartPos = function (e, d, g, c, b) {
                var a = this.chart, f = a.inverted, l = this.xAxis, n = l.reversed, r = f ? d.height / 2 : d.width / 2;
                e = (e = e.pointWidth) ? e / 2 : 0;
                l = f ? b.x : n ? -r - e : l.width - r + e;
                b = f ? n ? this.yAxis.height - r + e : -r - e : b.y;
                d.startXPos = l;
                d.startYPos = b;
                c ? "hidden" === d.visibility && (d.show(), d.attr({opacity: 0}).animate({opacity: 1})) : d.attr({opacity: 1}).animate({opacity: 0}, void 0, d.hide);
                a.hasRendered &&
                (g && d.attr({x: d.startXPos, y: d.startYPos}), d.placed = !0)
            };
            l.prototype.justifyDataLabel = function (e, d, g, c, b, a) {
                var f = this.chart, l = d.align, n = d.verticalAlign, r = e.box ? 0 : e.padding || 0;
                var h = g.x + r;
                if (0 > h) {
                    "right" === l ? (d.align = "left", d.inside = !0) : d.x = -h;
                    var t = !0
                }
                h = g.x + c.width - r;
                h > f.plotWidth && ("left" === l ? (d.align = "right", d.inside = !0) : d.x = f.plotWidth - h, t = !0);
                h = g.y + r;
                0 > h && ("bottom" === n ? (d.verticalAlign = "top", d.inside = !0) : d.y = -h, t = !0);
                h = g.y + c.height - r;
                h > f.plotHeight && ("top" === n ? (d.verticalAlign = "bottom", d.inside =
                    !0) : d.y = f.plotHeight - h, t = !0);
                t && (e.placed = !a, e.align(d, null, b));
                return t
            };
            n.pie && (n.pie.prototype.dataLabelPositioners = {
                radialDistributionY: function (e) {
                    return e.top + e.distributeBox.pos
                }, radialDistributionX: function (e, d, g, c) {
                    return e.getX(g < d.top + 2 || g > d.bottom - 2 ? c : g, d.half, d)
                }, justify: function (e, d, g) {
                    return g[0] + (e.half ? -1 : 1) * (d + e.labelDistance)
                }, alignToPlotEdges: function (e, d, g, c) {
                    e = e.getBBox().width;
                    return d ? e + c : g - e - c
                }, alignToConnectors: function (e, d, g, c) {
                    var b = 0, a;
                    e.forEach(function (c) {
                        a = c.dataLabel.getBBox().width;
                        a > b && (b = a)
                    });
                    return d ? b + c : g - b - c
                }
            }, n.pie.prototype.drawDataLabels = function () {
                var e = this, d = e.data, g, c = e.chart, b = e.options.dataLabels || {}, a = b.connectorPadding, n,
                    k = c.plotWidth, p = c.plotHeight, r = c.plotLeft, h = Math.round(c.chartWidth / 3), t,
                    I = e.center, A = I[2] / 2, N = I[1], x, m, y, B, u = [[], []], C, F, H, G, L = [0, 0, 0, 0],
                    P = e.dataLabelPositioners, Z;
                e.visible && (b.enabled || e._hasPointLabels) && (d.forEach(function (a) {
                    a.dataLabel && a.visible && a.dataLabel.shortened && (a.dataLabel.attr({width: "auto"}).css({
                        width: "auto",
                        textOverflow: "clip"
                    }),
                        a.dataLabel.shortened = !1)
                }), l.prototype.drawDataLabels.apply(e), d.forEach(function (a) {
                    a.dataLabel && (a.visible ? (u[a.half].push(a), a.dataLabel._pos = null, !M(b.style.width) && !M(a.options.dataLabels && a.options.dataLabels.style && a.options.dataLabels.style.width) && a.dataLabel.getBBox().width > h && (a.dataLabel.css({width: .7 * h}), a.dataLabel.shortened = !0)) : (a.dataLabel = a.dataLabel.destroy(), a.dataLabels && 1 === a.dataLabels.length && delete a.dataLabels))
                }), u.forEach(function (d, h) {
                    var l = d.length, t = [], n;
                    if (l) {
                        e.sortByAngle(d,
                            h - .5);
                        if (0 < e.maxLabelDistance) {
                            var w = Math.max(0, N - A - e.maxLabelDistance);
                            var z = Math.min(N + A + e.maxLabelDistance, c.plotHeight);
                            d.forEach(function (a) {
                                0 < a.labelDistance && a.dataLabel && (a.top = Math.max(0, N - A - a.labelDistance), a.bottom = Math.min(N + A + a.labelDistance, c.plotHeight), n = a.dataLabel.getBBox().height || 21, a.distributeBox = {
                                    target: a.labelPosition.natural.y - a.top + n / 2,
                                    size: n,
                                    rank: a.y
                                }, t.push(a.distributeBox))
                            });
                            w = z + n - w;
                            f.distribute(t, w, w / 5)
                        }
                        for (G = 0; G < l; G++) {
                            g = d[G];
                            y = g.labelPosition;
                            x = g.dataLabel;
                            H = !1 ===
                            g.visible ? "hidden" : "inherit";
                            F = w = y.natural.y;
                            t && M(g.distributeBox) && ("undefined" === typeof g.distributeBox.pos ? H = "hidden" : (B = g.distributeBox.size, F = P.radialDistributionY(g)));
                            delete g.positionIndex;
                            if (b.justify) C = P.justify(g, A, I); else switch (b.alignTo) {
                                case "connectors":
                                    C = P.alignToConnectors(d, h, k, r);
                                    break;
                                case "plotEdges":
                                    C = P.alignToPlotEdges(x, h, k, r);
                                    break;
                                default:
                                    C = P.radialDistributionX(e, g, F, w)
                            }
                            x._attr = {visibility: H, align: y.alignment};
                            Z = g.options.dataLabels || {};
                            x._pos = {
                                x: C + v(Z.x, b.x) + ({
                                    left: a,
                                    right: -a
                                }[y.alignment] || 0), y: F + v(Z.y, b.y) - 10
                            };
                            y.final.x = C;
                            y.final.y = F;
                            v(b.crop, !0) && (m = x.getBBox().width, w = null, C - m < a && 1 === h ? (w = Math.round(m - C + a), L[3] = Math.max(w, L[3])) : C + m > k - a && 0 === h && (w = Math.round(C + m - k + a), L[1] = Math.max(w, L[1])), 0 > F - B / 2 ? L[0] = Math.max(Math.round(-F + B / 2), L[0]) : F + B / 2 > p && (L[2] = Math.max(Math.round(F + B / 2 - p), L[2])), x.sideOverflow = w)
                        }
                    }
                }), 0 === q(L) || this.verifyDataLabelOverflow(L)) && (this.placeDataLabels(), this.points.forEach(function (a) {
                    Z = E(b, a.options.dataLabels);
                    if (n = v(Z.connectorWidth,
                        1)) {
                        var d;
                        t = a.connector;
                        if ((x = a.dataLabel) && x._pos && a.visible && 0 < a.labelDistance) {
                            H = x._attr.visibility;
                            if (d = !t) a.connector = t = c.renderer.path().addClass("highcharts-data-label-connector  highcharts-color-" + a.colorIndex + (a.className ? " " + a.className : "")).add(e.dataLabelsGroup), c.styledMode || t.attr({
                                "stroke-width": n,
                                stroke: Z.connectorColor || a.color || "#666666"
                            });
                            t[d ? "attr" : "animate"]({d: a.getConnectorPath()});
                            t.attr("visibility", H)
                        } else t && (a.connector = t.destroy())
                    }
                }))
            }, n.pie.prototype.placeDataLabels =
                function () {
                    this.points.forEach(function (e) {
                        var d = e.dataLabel, g;
                        d && e.visible && ((g = d._pos) ? (d.sideOverflow && (d._attr.width = Math.max(d.getBBox().width - d.sideOverflow, 0), d.css({
                            width: d._attr.width + "px",
                            textOverflow: (this.options.dataLabels.style || {}).textOverflow || "ellipsis"
                        }), d.shortened = !0), d.attr(d._attr), d[d.moved ? "animate" : "attr"](g), d.moved = !0) : d && d.attr({y: -9999}));
                        delete e.distributeBox
                    }, this)
                }, n.pie.prototype.alignDataLabel = k, n.pie.prototype.verifyDataLabelOverflow = function (e) {
                var d = this.center,
                    g = this.options, c = g.center, b = g.minSize || 80, a = null !== g.size;
                if (!a) {
                    if (null !== c[0]) var f = Math.max(d[2] - Math.max(e[1], e[3]), b); else f = Math.max(d[2] - e[1] - e[3], b), d[0] += (e[3] - e[1]) / 2;
                    null !== c[1] ? f = G(f, b, d[2] - Math.max(e[0], e[2])) : (f = G(f, b, d[2] - e[0] - e[2]), d[1] += (e[0] - e[2]) / 2);
                    f < d[2] ? (d[2] = f, d[3] = Math.min(B(g.innerSize || 0, f), f), this.translate(d), this.drawDataLabels && this.drawDataLabels()) : a = !0
                }
                return a
            });
            n.column && (n.column.prototype.alignDataLabel = function (e, d, g, c, b) {
                var a = this.chart.inverted, f = e.series,
                    n = e.dlBox || e.shapeArgs, k = v(e.below, e.plotY > v(this.translatedThreshold, f.yAxis.len)),
                    r = v(g.inside, !!this.options.stacking);
                n && (c = E(n), 0 > c.y && (c.height += c.y, c.y = 0), n = c.y + c.height - f.yAxis.len, 0 < n && n < c.height && (c.height -= n), a && (c = {
                    x: f.yAxis.len - c.y - c.height,
                    y: f.xAxis.len - c.x - c.width,
                    width: c.height,
                    height: c.width
                }), r || (a ? (c.x += k ? 0 : c.width, c.width = 0) : (c.y += k ? c.height : 0, c.height = 0)));
                g.align = v(g.align, !a || r ? "center" : k ? "right" : "left");
                g.verticalAlign = v(g.verticalAlign, a || r ? "middle" : k ? "top" : "bottom");
                l.prototype.alignDataLabel.call(this,
                    e, d, g, c, b);
                g.inside && e.contrastColor && d.css({color: e.contrastColor})
            })
        });
    P(u, "modules/overlapping-datalabels.src.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent, q = k.fireEvent, G = k.isArray, M = k.objectEach, L = k.pick;
        f = f.Chart;
        u(f, "render", function () {
            var f = [];
            (this.labelCollectors || []).forEach(function (k) {
                f = f.concat(k())
            });
            (this.yAxis || []).forEach(function (k) {
                k.options.stackLabels && !k.options.stackLabels.allowOverlap && M(k.stacks, function (k) {
                    M(k, function (k) {
                        f.push(k.label)
                    })
                })
            });
            (this.series || []).forEach(function (k) {
                var q = k.options.dataLabels;
                k.visible && (!1 !== q.enabled || k._hasPointLabels) && (k.nodes || k.points).forEach(function (k) {
                    k.visible && (G(k.dataLabels) ? k.dataLabels : k.dataLabel ? [k.dataLabel] : []).forEach(function (v) {
                        var q = v.options;
                        v.labelrank = L(q.labelrank, k.labelrank, k.shapeArgs && k.shapeArgs.height);
                        q.allowOverlap || f.push(v)
                    })
                })
            });
            this.hideOverlappingLabels(f)
        });
        f.prototype.hideOverlappingLabels = function (f) {
            var k = this, E = f.length, A = k.renderer, v, B, y, p = !1;
            var l = function (d) {
                var e =
                    d.box ? 0 : d.padding || 0;
                var c = 0;
                if (d && (!d.alignAttr || d.placed)) {
                    var b = d.alignAttr || {x: d.attr("x"), y: d.attr("y")};
                    var a = d.parentGroup;
                    d.width || (c = d.getBBox(), d.width = c.width, d.height = c.height, c = A.fontMetrics(null, d.element).h);
                    return {
                        x: b.x + (a.translateX || 0) + e,
                        y: b.y + (a.translateY || 0) + e - c,
                        width: d.width - 2 * e,
                        height: d.height - 2 * e
                    }
                }
            };
            for (B = 0; B < E; B++) if (v = f[B]) v.oldOpacity = v.opacity, v.newOpacity = 1, v.absoluteBox = l(v);
            f.sort(function (d, e) {
                return (e.labelrank || 0) - (d.labelrank || 0)
            });
            for (B = 0; B < E; B++) {
                var n = (l = f[B]) &&
                    l.absoluteBox;
                for (v = B + 1; v < E; ++v) {
                    var e = (y = f[v]) && y.absoluteBox;
                    !n || !e || l === y || 0 === l.newOpacity || 0 === y.newOpacity || e.x > n.x + n.width || e.x + e.width < n.x || e.y > n.y + n.height || e.y + e.height < n.y || ((l.labelrank < y.labelrank ? l : y).newOpacity = 0)
                }
            }
            f.forEach(function (d) {
                var e;
                if (d) {
                    var c = d.newOpacity;
                    d.oldOpacity !== c && (d.alignAttr && d.placed ? (c ? d.show(!0) : e = function () {
                        d.hide(!0);
                        d.placed = !1
                    }, p = !0, d.alignAttr.opacity = c, d[d.isOld ? "animate" : "attr"](d.alignAttr, null, e), q(k, "afterHideOverlappingLabel")) : d.attr({opacity: c}));
                    d.isOld = !0
                }
            });
            p && q(k, "afterHideAllOverlappingLabels")
        }
    });
    P(u, "parts/Interaction.js", [u["parts/Globals.js"], u["parts/Legend.js"], u["parts/Point.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        var H = q.addEvent, M = q.createElement, L = q.css, C = q.defined, F = q.extend, E = q.fireEvent, A = q.isArray,
            v = q.isFunction, B = q.isObject, y = q.merge, p = q.objectEach, l = q.pick;
        q = f.Chart;
        var n = f.defaultOptions, e = f.defaultPlotOptions, d = f.hasTouch, g = f.Series, c = f.seriesTypes, b = f.svg;
        f = f.TrackerMixin = {
            drawTrackerPoint: function () {
                var a =
                    this, b = a.chart, c = b.pointer, e = function (a) {
                    var b = c.getPointFromEvent(a);
                    "undefined" !== typeof b && (c.isDirectTouch = !0, b.onMouseOver(a))
                }, g;
                a.points.forEach(function (a) {
                    g = A(a.dataLabels) ? a.dataLabels : a.dataLabel ? [a.dataLabel] : [];
                    a.graphic && (a.graphic.element.point = a);
                    g.forEach(function (b) {
                        b.div ? b.div.point = a : b.element.point = a
                    })
                });
                a._hasTracking || (a.trackerGroups.forEach(function (h) {
                    if (a[h]) {
                        a[h].addClass("highcharts-tracker").on("mouseover", e).on("mouseout", function (a) {
                            c.onTrackerMouseOut(a)
                        });
                        if (d) a[h].on("touchstart",
                            e);
                        !b.styledMode && a.options.cursor && a[h].css(L).css({cursor: a.options.cursor})
                    }
                }), a._hasTracking = !0);
                E(this, "afterDrawTracker")
            }, drawTrackerGraph: function () {
                var a = this, c = a.options, e = c.trackByArea, g = [].concat(e ? a.areaPath : a.graphPath),
                    f = g.length, h = a.chart, l = h.pointer, n = h.renderer, k = h.options.tooltip.snap, p = a.tracker,
                    x, m = function () {
                        if (h.hoverSeries !== a) a.onMouseOver()
                    }, v = "rgba(192,192,192," + (b ? .0001 : .002) + ")";
                if (f && !e) for (x = f + 1; x--;) "M" === g[x] && g.splice(x + 1, 0, g[x + 1] - k, g[x + 2], "L"), (x && "M" === g[x] || x ===
                    f) && g.splice(x, 0, "L", g[x - 2] + k, g[x - 1]);
                p ? p.attr({d: g}) : a.graph && (a.tracker = n.path(g).attr({
                    visibility: a.visible ? "visible" : "hidden",
                    zIndex: 2
                }).addClass(e ? "highcharts-tracker-area" : "highcharts-tracker-line").add(a.group), h.styledMode || a.tracker.attr({
                    "stroke-linejoin": "round",
                    stroke: v,
                    fill: e ? v : "none",
                    "stroke-width": a.graph.strokeWidth() + (e ? 0 : 2 * k)
                }), [a.tracker, a.markerGroup].forEach(function (a) {
                    a.addClass("highcharts-tracker").on("mouseover", m).on("mouseout", function (a) {
                        l.onTrackerMouseOut(a)
                    });
                    c.cursor &&
                    !h.styledMode && a.css({cursor: c.cursor});
                    if (d) a.on("touchstart", m)
                }));
                E(this, "afterDrawTracker")
            }
        };
        c.column && (c.column.prototype.drawTracker = f.drawTrackerPoint);
        c.pie && (c.pie.prototype.drawTracker = f.drawTrackerPoint);
        c.scatter && (c.scatter.prototype.drawTracker = f.drawTrackerPoint);
        F(k.prototype, {
            setItemEvents: function (a, b, c) {
                var d = this, e = d.chart.renderer.boxWrapper, h = a instanceof u,
                    g = "highcharts-legend-" + (h ? "point" : "series") + "-active", f = d.chart.styledMode;
                (c ? [b, a.legendSymbol] : [a.legendGroup]).forEach(function (c) {
                    if (c) c.on("mouseover",
                        function () {
                            a.visible && d.allItems.forEach(function (b) {
                                a !== b && b.setState("inactive", !h)
                            });
                            a.setState("hover");
                            a.visible && e.addClass(g);
                            f || b.css(d.options.itemHoverStyle)
                        }).on("mouseout", function () {
                        d.chart.styledMode || b.css(y(a.visible ? d.itemStyle : d.itemHiddenStyle));
                        d.allItems.forEach(function (b) {
                            a !== b && b.setState("", !h)
                        });
                        e.removeClass(g);
                        a.setState()
                    }).on("click", function (b) {
                        var c = function () {
                            a.setVisible && a.setVisible();
                            d.allItems.forEach(function (b) {
                                a !== b && b.setState(a.visible ? "inactive" : "", !h)
                            })
                        };
                        e.removeClass(g);
                        b = {browserEvent: b};
                        a.firePointEvent ? a.firePointEvent("legendItemClick", b, c) : E(a, "legendItemClick", b, c)
                    })
                })
            }, createCheckboxForItem: function (a) {
                a.checkbox = M("input", {
                    type: "checkbox",
                    className: "highcharts-legend-checkbox",
                    checked: a.selected,
                    defaultChecked: a.selected
                }, this.options.itemCheckboxStyle, this.chart.container);
                H(a.checkbox, "click", function (b) {
                    E(a.series || a, "checkboxClick", {checked: b.target.checked, item: a}, function () {
                        a.select()
                    })
                })
            }
        });
        F(q.prototype, {
            showResetZoom: function () {
                function a() {
                    b.zoomOut()
                }

                var b = this, c = n.lang, d = b.options.chart.resetZoomButton, e = d.theme, h = e.states,
                    g = "chart" === d.relativeTo || "spaceBox" === d.relativeTo ? null : "plotBox";
                E(this, "beforeShowResetZoom", null, function () {
                    b.resetZoomButton = b.renderer.button(c.resetZoom, null, null, a, e, h && h.hover).attr({
                        align: d.position.align,
                        title: c.resetZoomTitle
                    }).addClass("highcharts-reset-zoom").add().align(d.position, !1, g)
                });
                E(this, "afterShowResetZoom")
            }, zoomOut: function () {
                E(this, "selection", {resetSelection: !0}, this.zoom)
            }, zoom: function (a) {
                var b =
                    this, c, d = b.pointer, e = !1, h = b.inverted ? d.mouseDownX : d.mouseDownY;
                !a || a.resetSelection ? (b.axes.forEach(function (a) {
                    c = a.zoom()
                }), d.initiated = !1) : a.xAxis.concat(a.yAxis).forEach(function (a) {
                    var g = a.axis, f = b.inverted ? g.left : g.top, l = b.inverted ? f + g.width : f + g.height,
                        m = g.isXAxis, t = !1;
                    if (!m && h >= f && h <= l || m || !C(h)) t = !0;
                    d[m ? "zoomX" : "zoomY"] && t && (c = g.zoom(a.min, a.max), g.displayBtn && (e = !0))
                });
                var g = b.resetZoomButton;
                e && !g ? b.showResetZoom() : !e && B(g) && (b.resetZoomButton = g.destroy());
                c && b.redraw(l(b.options.chart.animation,
                    a && a.animation, 100 > b.pointCount))
            }, pan: function (a, b) {
                var c = this, d = c.hoverPoints, e = c.options.chart, h;
                b = "object" === typeof b ? b : {enabled: b, type: "x"};
                e && e.panning && (e.panning = b);
                var g = b.type;
                E(this, "pan", {originalEvent: a}, function () {
                    d && d.forEach(function (a) {
                        a.setState()
                    });
                    var b = [1];
                    "xy" === g ? b = [1, 0] : "y" === g && (b = [0]);
                    b.forEach(function (b) {
                        var d = c[b ? "xAxis" : "yAxis"][0], e = d.options, g = d.horiz, f = a[g ? "chartX" : "chartY"];
                        g = g ? "mouseDownX" : "mouseDownY";
                        var l = c[g], t = (d.pointRange || 0) / 2, n = d.reversed && !c.inverted ||
                        !d.reversed && c.inverted ? -1 : 1, r = d.getExtremes(), k = d.toValue(l - f, !0) + t * n;
                        n = d.toValue(l + d.len - f, !0) - t * n;
                        var p = n < k;
                        l = p ? n : k;
                        k = p ? k : n;
                        n = Math.min(r.dataMin, t ? r.min : d.toValue(d.toPixels(r.min) - d.minPixelPadding));
                        t = Math.max(r.dataMax, t ? r.max : d.toValue(d.toPixels(r.max) + d.minPixelPadding));
                        if (!e.ordinal) {
                            b && (e = n - l, 0 < e && (k += e, l = n), e = k - t, 0 < e && (k = t, l -= e));
                            if (d.series.length && l !== r.min && k !== r.max && b || d.panningState && l >= d.panningState.startMin && k <= d.panningState.startMax) d.setExtremes(l, k, !1, !1, {trigger: "pan"}),
                                h = !0;
                            c[g] = f
                        }
                    });
                    h && c.redraw(!1);
                    L(c.container, {cursor: "move"})
                })
            }
        });
        F(u.prototype, {
            select: function (a, b) {
                var c = this, d = c.series, e = d.chart;
                this.selectedStaging = a = l(a, !c.selected);
                c.firePointEvent(a ? "select" : "unselect", {accumulate: b}, function () {
                    c.selected = c.options.selected = a;
                    d.options.data[d.data.indexOf(c)] = c.options;
                    c.setState(a && "select");
                    b || e.getSelectedPoints().forEach(function (a) {
                        var b = a.series;
                        a.selected && a !== c && (a.selected = a.options.selected = !1, b.options.data[b.data.indexOf(a)] = a.options, a.setState(e.hoverPoints &&
                        b.options.inactiveOtherPoints ? "inactive" : ""), a.firePointEvent("unselect"))
                    })
                });
                delete this.selectedStaging
            }, onMouseOver: function (a) {
                var b = this.series.chart, c = b.pointer;
                a = a ? c.normalize(a) : c.getChartCoordinatesFromPoint(this, b.inverted);
                c.runPointActions(a, this)
            }, onMouseOut: function () {
                var a = this.series.chart;
                this.firePointEvent("mouseOut");
                this.series.options.inactiveOtherPoints || (a.hoverPoints || []).forEach(function (a) {
                    a.setState()
                });
                a.hoverPoints = a.hoverPoint = null
            }, importEvents: function () {
                if (!this.hasImportedEvents) {
                    var a =
                        this, b = y(a.series.options.point, a.options).events;
                    a.events = b;
                    p(b, function (b, c) {
                        v(b) && H(a, c, b)
                    });
                    this.hasImportedEvents = !0
                }
            }, setState: function (a, b) {
                var c = this.series, d = this.state, g = c.options.states[a || "normal"] || {},
                    h = e[c.type].marker && c.options.marker, f = h && !1 === h.enabled,
                    n = h && h.states && h.states[a || "normal"] || {}, k = !1 === n.enabled, p = c.stateMarkerGraphic,
                    w = this.marker || {}, m = c.chart, v = c.halo, q, A = h && c.markerAttribs;
                a = a || "";
                if (!(a === this.state && !b || this.selected && "select" !== a || !1 === g.enabled || a && (k || f && !1 ===
                    n.enabled) || a && w.states && w.states[a] && !1 === w.states[a].enabled)) {
                    this.state = a;
                    A && (q = c.markerAttribs(this, a));
                    if (this.graphic) {
                        d && this.graphic.removeClass("highcharts-point-" + d);
                        a && this.graphic.addClass("highcharts-point-" + a);
                        if (!m.styledMode) {
                            var y = c.pointAttribs(this, a);
                            var B = l(m.options.chart.animation, g.animation);
                            c.options.inactiveOtherPoints && ((this.dataLabels || []).forEach(function (a) {
                                a && a.animate({opacity: y.opacity}, B)
                            }), this.connector && this.connector.animate({opacity: y.opacity}, B));
                            this.graphic.animate(y,
                                B)
                        }
                        q && this.graphic.animate(q, l(m.options.chart.animation, n.animation, h.animation));
                        p && p.hide()
                    } else {
                        if (a && n) {
                            d = w.symbol || c.symbol;
                            p && p.currentSymbol !== d && (p = p.destroy());
                            if (q) if (p) p[b ? "animate" : "attr"]({
                                x: q.x,
                                y: q.y
                            }); else d && (c.stateMarkerGraphic = p = m.renderer.symbol(d, q.x, q.y, q.width, q.height).add(c.markerGroup), p.currentSymbol = d);
                            !m.styledMode && p && p.attr(c.pointAttribs(this, a))
                        }
                        p && (p[a && this.isInside ? "show" : "hide"](), p.element.point = this)
                    }
                    a = g.halo;
                    g = (p = this.graphic || p) && p.visibility || "inherit";
                    a &&
                    a.size && p && "hidden" !== g && !this.isCluster ? (v || (c.halo = v = m.renderer.path().add(p.parentGroup)), v.show()[b ? "animate" : "attr"]({d: this.haloPath(a.size)}), v.attr({
                        "class": "highcharts-halo highcharts-color-" + l(this.colorIndex, c.colorIndex) + (this.className ? " " + this.className : ""),
                        visibility: g,
                        zIndex: -1
                    }), v.point = this, m.styledMode || v.attr(F({
                        fill: this.color || c.color,
                        "fill-opacity": a.opacity
                    }, a.attributes))) : v && v.point && v.point.haloPath && v.animate({d: v.point.haloPath(0)}, null, v.hide);
                    E(this, "afterSetState")
                }
            },
            haloPath: function (a) {
                return this.series.chart.renderer.symbols.circle(Math.floor(this.plotX) - a, this.plotY - a, 2 * a, 2 * a)
            }
        });
        F(g.prototype, {
            onMouseOver: function () {
                var a = this.chart, b = a.hoverSeries;
                if (b && b !== this) b.onMouseOut();
                this.options.events.mouseOver && E(this, "mouseOver");
                this.setState("hover");
                a.hoverSeries = this
            }, onMouseOut: function () {
                var a = this.options, b = this.chart, c = b.tooltip, d = b.hoverPoint;
                b.hoverSeries = null;
                if (d) d.onMouseOut();
                this && a.events.mouseOut && E(this, "mouseOut");
                !c || this.stickyTracking ||
                c.shared && !this.noSharedTooltip || c.hide();
                b.series.forEach(function (a) {
                    a.setState("", !0)
                })
            }, setState: function (a, b) {
                var c = this, d = c.options, e = c.graph, h = d.inactiveOtherPoints, g = d.states, f = d.lineWidth,
                    n = d.opacity,
                    k = l(g[a || "normal"] && g[a || "normal"].animation, c.chart.options.chart.animation);
                d = 0;
                a = a || "";
                if (c.state !== a && ([c.group, c.markerGroup, c.dataLabelsGroup].forEach(function (b) {
                    b && (c.state && b.removeClass("highcharts-series-" + c.state), a && b.addClass("highcharts-series-" + a))
                }), c.state = a, !c.chart.styledMode)) {
                    if (g[a] &&
                        !1 === g[a].enabled) return;
                    a && (f = g[a].lineWidth || f + (g[a].lineWidthPlus || 0), n = l(g[a].opacity, n));
                    if (e && !e.dashstyle) for (g = {"stroke-width": f}, e.animate(g, k); c["zone-graph-" + d];) c["zone-graph-" + d].attr(g), d += 1;
                    h || [c.group, c.markerGroup, c.dataLabelsGroup, c.labelBySeries].forEach(function (a) {
                        a && a.animate({opacity: n}, k)
                    })
                }
                b && h && c.points && c.setAllPointsToState(a)
            }, setAllPointsToState: function (a) {
                this.points.forEach(function (b) {
                    b.setState && b.setState(a)
                })
            }, setVisible: function (a, b) {
                var c = this, d = c.chart, e = c.legendItem,
                    h = d.options.chart.ignoreHiddenSeries, g = c.visible;
                var f = (c.visible = a = c.options.visible = c.userOptions.visible = "undefined" === typeof a ? !g : a) ? "show" : "hide";
                ["group", "dataLabelsGroup", "markerGroup", "tracker", "tt"].forEach(function (a) {
                    if (c[a]) c[a][f]()
                });
                if (d.hoverSeries === c || (d.hoverPoint && d.hoverPoint.series) === c) c.onMouseOut();
                e && d.legend.colorizeItem(c, a);
                c.isDirty = !0;
                c.options.stacking && d.series.forEach(function (a) {
                    a.options.stacking && a.visible && (a.isDirty = !0)
                });
                c.linkedSeries.forEach(function (b) {
                    b.setVisible(a,
                        !1)
                });
                h && (d.isDirtyBox = !0);
                E(c, f);
                !1 !== b && d.redraw()
            }, show: function () {
                this.setVisible(!0)
            }, hide: function () {
                this.setVisible(!1)
            }, select: function (a) {
                this.selected = a = this.options.selected = "undefined" === typeof a ? !this.selected : a;
                this.checkbox && (this.checkbox.checked = a);
                E(this, a ? "select" : "unselect")
            }, drawTracker: f.drawTrackerGraph
        })
    });
    P(u, "parts/Responsive.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.find, q = k.isArray, G = k.isObject, M = k.merge, L = k.objectEach, C = k.pick, F = k.splat,
            E = k.uniqueKey;
        f = f.Chart;
        f.prototype.setResponsive = function (f, k) {
            var v = this.options.responsive, q = [], p = this.currentResponsive;
            !k && v && v.rules && v.rules.forEach(function (f) {
                "undefined" === typeof f._id && (f._id = E());
                this.matchResponsiveRule(f, q)
            }, this);
            k = M.apply(0, q.map(function (f) {
                return u(v.rules, function (l) {
                    return l._id === f
                }).chartOptions
            }));
            k.isResponsiveOptions = !0;
            q = q.toString() || void 0;
            q !== (p && p.ruleIds) && (p && this.update(p.undoOptions, f, !0), q ? (p = this.currentOptions(k), p.isResponsiveOptions = !0, this.currentResponsive =
                {
                    ruleIds: q,
                    mergedOptions: k,
                    undoOptions: p
                }, this.update(k, f, !0)) : this.currentResponsive = void 0)
        };
        f.prototype.matchResponsiveRule = function (f, k) {
            var v = f.condition;
            (v.callback || function () {
                return this.chartWidth <= C(v.maxWidth, Number.MAX_VALUE) && this.chartHeight <= C(v.maxHeight, Number.MAX_VALUE) && this.chartWidth >= C(v.minWidth, 0) && this.chartHeight >= C(v.minHeight, 0)
            }).call(this) && k.push(f._id)
        };
        f.prototype.currentOptions = function (f) {
            function k(f, l, n, e) {
                var d;
                L(f, function (g, c) {
                    if (!e && -1 < A.collectionsWithUpdate.indexOf(c)) for (g =
                                                                                F(g), n[c] = [], d = 0; d < g.length; d++) l[c][d] && (n[c][d] = {}, k(g[d], l[c][d], n[c][d], e + 1)); else G(g) ? (n[c] = q(g) ? [] : {}, k(g, l[c] || {}, n[c], e + 1)) : n[c] = "undefined" === typeof l[c] ? null : l[c]
                })
            }

            var A = this, y = {};
            k(f, this.options, y, 0);
            return y
        }
    });
    P(u, "masters/highcharts.src.js", [u["parts/Globals.js"]], function (f) {
        return f
    });
    P(u, "parts-gantt/CurrentDateIndicator.js", [u["parts/Globals.js"], u["parts/Utilities.js"], u["parts/PlotLineOrBand.js"]], function (f, k, u) {
        var q = k.addEvent, H = k.merge;
        k = k.wrap;
        var M = {
            currentDateIndicator: !0,
            color: "#ccd6eb", width: 2, label: {
                format: "%a, %b %d %Y, %H:%M", formatter: function (k, q) {
                    return f.dateFormat(q, k)
                }, rotation: 0, style: {fontSize: "10px"}
            }
        };
        q(f.Axis, "afterSetOptions", function () {
            var f = this.options, k = f.currentDateIndicator;
            k && (k = "object" === typeof k ? H(M, k) : H(M), k.value = new Date, f.plotLines || (f.plotLines = []), f.plotLines.push(k))
        });
        q(u, "render", function () {
            this.label && this.label.attr({text: this.getLabelText(this.options.label)})
        });
        k(u.prototype, "getLabelText", function (f, k) {
            var q = this.options;
            return q.currentDateIndicator &&
            q.label && "function" === typeof q.label.formatter ? (q.value = new Date, q.label.formatter.call(this, q.value, q.label.format)) : f.call(this, k)
        })
    });
    P(u, "parts-gantt/GridAxis.js", [u["parts/Globals.js"], u["parts/Tick.js"], u["parts/Utilities.js"]], function (f, k, u) {
        var q = u.addEvent, H = u.defined, M = u.erase, L = u.find, C = u.isArray, F = u.isNumber, E = u.merge,
            A = u.pick, v = u.timeUnits, B = u.wrap, y = f.dateFormat, p = function (c) {
                return u.isObject(c, !0)
            }, l = f.Chart, n = f.Axis, e = function (c) {
                var b = c.options;
                b.labels || (b.labels = {});
                b.labels.align =
                    A(b.labels.align, "center");
                c.categories || (b.showLastLabel = !1);
                c.labelRotation = 0;
                b.labels.rotation = 0
            }, d = {top: 0, right: 1, bottom: 2, left: 3, 0: "top", 1: "right", 2: "bottom", 3: "left"};
        n.prototype.isOuterAxis = function () {
            var c = this, b = c.columnIndex, a = c.linkedParent && c.linkedParent.columns || c.columns,
                d = b ? c.linkedParent : c, e = -1, g = 0;
            c.chart[c.coll].forEach(function (a, b) {
                a.side !== c.side || a.options.isInternal || (g = b, a === d && (e = b))
            });
            return g === e && (F(b) ? a.length === b : !0)
        };
        n.prototype.getMaxLabelDimensions = function (c, b) {
            var a =
                {width: 0, height: 0};
            b.forEach(function (b) {
                b = c[b];
                if (p(b)) {
                    var d = p(b.label) ? b.label : {};
                    b = d.getBBox ? d.getBBox().height : 0;
                    d.textStr && !F(d.textPxLength) && (d.textPxLength = d.getBBox().width);
                    d = F(d.textPxLength) ? Math.round(d.textPxLength) : 0;
                    a.height = Math.max(b, a.height);
                    a.width = Math.max(d, a.width)
                }
            });
            return a
        };
        f.dateFormats.W = function (c) {
            c = new this.Date(c);
            var b = (this.get("Day", c) + 6) % 7, a = new this.Date(c.valueOf());
            this.set("Date", a, this.get("Date", c) - b + 3);
            b = new this.Date(this.get("FullYear", a), 0, 1);
            4 !== this.get("Day",
                b) && (this.set("Month", c, 0), this.set("Date", c, 1 + (11 - this.get("Day", b)) % 7));
            return (1 + Math.floor((a.valueOf() - b.valueOf()) / 6048E5)).toString()
        };
        f.dateFormats.E = function (c) {
            return y("%a", c, !0).charAt(0)
        };
        q(k, "afterGetLabelPosition", function (c) {
            var b = this.label, a = this.axis, e = a.reversed, g = a.chart, f = a.options,
                l = f && p(f.grid) ? f.grid : {};
            f = a.options.labels;
            var h = f.align, n = d[a.side], k = c.tickmarkOffset, v = a.tickPositions, q = this.pos - k;
            v = F(v[c.index + 1]) ? v[c.index + 1] - k : a.max + k;
            var x = a.tickSize("tick", !0);
            k = C(x) ? x[0] :
                0;
            x = x && x[1] / 2;
            if (!0 === l.enabled) {
                if ("top" === n) {
                    l = a.top + a.offset;
                    var m = l - k
                } else "bottom" === n ? (m = g.chartHeight - a.bottom + a.offset, l = m + k) : (l = a.top + a.len - a.translate(e ? v : q), m = a.top + a.len - a.translate(e ? q : v));
                "right" === n ? (n = g.chartWidth - a.right + a.offset, e = n + k) : "left" === n ? (e = a.left + a.offset, n = e - k) : (n = Math.round(a.left + a.translate(e ? v : q)) - x, e = Math.round(a.left + a.translate(e ? q : v)) - x);
                this.slotWidth = e - n;
                c.pos.x = "left" === h ? n : "right" === h ? e : n + (e - n) / 2;
                c.pos.y = m + (l - m) / 2;
                g = g.renderer.fontMetrics(f.style.fontSize, b.element);
                b = b.getBBox().height;
                f.useHTML ? c.pos.y += g.b + -(b / 2) : (b = Math.round(b / g.h), c.pos.y += (g.b - (g.h - g.f)) / 2 + -((b - 1) * g.h / 2));
                c.pos.x += a.horiz && f.x || 0
            }
        });
        q(n, "afterTickSize", function (c) {
            var b = this.defaultLeftAxisOptions, a = this.horiz, d = this.options.grid;
            d = void 0 === d ? {} : d;
            var e = this.maxLabelDimensions;
            d.enabled && (b = 2 * Math.abs(b.labels.x), a = a ? d.cellHeight || b + e.height : b + e.width, C(c.tickSize) ? c.tickSize[0] = a : c.tickSize = [a])
        });
        q(n, "afterGetTitlePosition", function (c) {
            var b = this.options;
            if (!0 === (b && p(b.grid) ? b.grid :
                {}).enabled) {
                var a = this.axisTitle, e = a && a.getBBox().width, g = this.horiz, f = this.left, l = this.top,
                    h = this.width, n = this.height, k = b.title;
                b = this.opposite;
                var v = this.offset, q = this.tickSize() || [0], x = k.x || 0, m = k.y || 0,
                    y = A(k.margin, g ? 5 : 10);
                a = this.chart.renderer.fontMetrics(k.style && k.style.fontSize, a).f;
                q = (g ? l + n : f) + q[0] / 2 * (b ? -1 : 1) * (g ? 1 : -1) + (this.side === d.bottom ? a : 0);
                c.titlePosition.x = g ? f - e / 2 - y + x : q + (b ? h : 0) + v + x;
                c.titlePosition.y = g ? q - (b ? n : 0) + (b ? a : -a) / 2 + v + m : l - y + m
            }
        });
        B(n.prototype, "unsquish", function (c) {
            var b = this.options;
            return !0 === (b && p(b.grid) ? b.grid : {}).enabled && this.categories ? this.tickInterval : c.apply(this, Array.prototype.slice.call(arguments, 1))
        });
        q(n, "afterSetOptions", function (c) {
            var b = this.options;
            c = c.userOptions;
            var a = b && p(b.grid) ? b.grid : {};
            if (!0 === a.enabled) {
                var d = E(!0, {
                    className: "highcharts-grid-axis " + (c.className || ""),
                    dateTimeLabelFormats: {
                        hour: {list: ["%H:%M", "%H"]},
                        day: {list: ["%A, %e. %B", "%a, %e. %b", "%E"]},
                        week: {list: ["Week %W", "W%W"]},
                        month: {list: ["%B", "%b", "%o"]}
                    },
                    grid: {borderWidth: 1},
                    labels: {
                        padding: 2,
                        style: {fontSize: "13px"}
                    },
                    margin: 0,
                    title: {text: null, reserveSpace: !1, rotation: 0},
                    units: [["millisecond", [1, 10, 100]], ["second", [1, 10]], ["minute", [1, 5, 15]], ["hour", [1, 6]], ["day", [1]], ["week", [1]], ["month", [1]], ["year", null]]
                }, c);
                "xAxis" === this.coll && (H(c.linkedTo) && !H(c.tickPixelInterval) && (d.tickPixelInterval = 350), H(c.tickPixelInterval) || !H(c.linkedTo) || H(c.tickPositioner) || H(c.tickInterval) || (d.tickPositioner = function (a, b) {
                    var c = this.linkedParent && this.linkedParent.tickPositions && this.linkedParent.tickPositions.info;
                    if (c) {
                        var e, g = d.units;
                        for (e = 0; e < g.length; e++) if (g[e][0] === c.unitName) {
                            var f = e;
                            break
                        }
                        if (g[f + 1]) {
                            var l = g[f + 1][0];
                            var n = (g[f + 1][1] || [1])[0]
                        } else "year" === c.unitName && (l = "year", n = 10 * c.count);
                        c = v[l];
                        this.tickInterval = c * n;
                        return this.getTimeTicks({unitRange: c, count: n, unitName: l}, a, b, this.options.startOfWeek)
                    }
                }));
                E(!0, this.options, d);
                this.horiz && (b.minPadding = A(c.minPadding, 0), b.maxPadding = A(c.maxPadding, 0));
                F(b.grid.borderWidth) && (b.tickWidth = b.lineWidth = a.borderWidth)
            }
        });
        q(n, "afterSetAxisTranslation", function () {
            var c =
                    this.options, b = c && p(c.grid) ? c.grid : {}, a = this.tickPositions && this.tickPositions.info,
                d = this.userOptions.labels || {};
            this.horiz && (!0 === b.enabled && this.series.forEach(function (a) {
                a.options.pointRange = 0
            }), a && (!1 === c.dateTimeLabelFormats[a.unitName].range || 1 < a.count) && !H(d.align) && (c.labels.align = "left", H(d.x) || (c.labels.x = 3)))
        });
        q(n, "trimTicks", function () {
            var c = this.options, b = c && p(c.grid) ? c.grid : {}, a = this.categories, d = this.tickPositions,
                e = d[0], g = d[d.length - 1], f = this.linkedParent && this.linkedParent.min ||
                this.min, h = this.linkedParent && this.linkedParent.max || this.max, l = this.tickInterval;
            !0 !== b.enabled || a || !this.horiz && !this.isLinked || (e < f && e + l > f && !c.startOnTick && (d[0] = f), g > h && g - l < h && !c.endOnTick && (d[d.length - 1] = h))
        });
        q(n, "afterRender", function () {
            var c = this.options, b = c && p(c.grid) ? c.grid : {}, a = this.chart.renderer;
            if (!0 === b.enabled) {
                this.maxLabelDimensions = this.getMaxLabelDimensions(this.ticks, this.tickPositions);
                this.rightWall && this.rightWall.destroy();
                if (this.isOuterAxis() && this.axisLine) {
                    var e = c.lineWidth;
                    if (e) {
                        var g = this.getLinePath(e);
                        var f = g.indexOf("M") + 1;
                        var l = g.indexOf("L") + 1;
                        b = g.indexOf("M") + 2;
                        var h = g.indexOf("L") + 2;
                        var n = (this.tickSize("tick")[0] - 1) * (this.side === d.top || this.side === d.left ? -1 : 1);
                        this.horiz ? (g[b] += n, g[h] += n) : (g[f] += n, g[l] += n);
                        this.axisLineExtra ? this.axisLineExtra.animate({d: g}) : (this.axisLineExtra = a.path(g).attr({zIndex: 7}).addClass("highcharts-axis-line").add(this.axisGroup), a.styledMode || this.axisLineExtra.attr({
                            stroke: c.lineColor,
                            "stroke-width": e
                        }));
                        this.axisLine[this.showAxis ?
                            "show" : "hide"](!0)
                    }
                }
                (this.columns || []).forEach(function (a) {
                    a.render()
                })
            }
        });
        var g = {
            afterGetOffset: function () {
                (this.columns || []).forEach(function (c) {
                    c.getOffset()
                })
            }, afterInit: function () {
                var c = this.chart, b = this.userOptions, a = this.options;
                a = a && p(a.grid) ? a.grid : {};
                a.enabled && (e(this), B(this, "labelFormatter", function (a) {
                    var b = this.axis, c = b.tickPositions, d = this.value,
                        e = (b.isLinked ? b.linkedParent : b).series[0], g = d === c[0];
                    c = d === c[c.length - 1];
                    e = e && L(e.options.data, function (a) {
                        return a[b.isXAxis ? "x" : "y"] ===
                            d
                    });
                    this.isFirst = g;
                    this.isLast = c;
                    this.point = e;
                    return a.call(this)
                }));
                if (a.columns) for (var d = this.columns = [], g = this.columnIndex = 0; ++g < a.columns.length;) {
                    var f = E(b, a.columns[a.columns.length - g - 1], {linkedTo: 0, type: "category"});
                    delete f.grid.columns;
                    f = new n(this.chart, f, !0);
                    f.isColumn = !0;
                    f.columnIndex = g;
                    M(c.axes, f);
                    M(c[this.coll], f);
                    d.push(f)
                }
            }, afterSetOptions: function (c) {
                c = (c = c.userOptions) && p(c.grid) ? c.grid : {};
                var b = c.columns;
                c.enabled && b && E(!0, this.options, b[b.length - 1])
            }, afterSetScale: function () {
                (this.columns ||
                    []).forEach(function (c) {
                    c.setScale()
                })
            }, destroy: function (c) {
                (this.columns || []).forEach(function (b) {
                    b.destroy(c.keepEvents)
                })
            }, init: function (c) {
                var b = (c = c.userOptions) && p(c.grid) ? c.grid : {};
                b.enabled && H(b.borderColor) && (c.tickColor = c.lineColor = b.borderColor)
            }
        };
        Object.keys(g).forEach(function (c) {
            q(n, c, g[c])
        });
        q(l, "afterSetChartSize", function () {
            this.axes.forEach(function (c) {
                (c.columns || []).forEach(function (b) {
                    b.setAxisSize();
                    b.setAxisTranslation()
                })
            })
        })
    });
    P(u, "modules/static-scale.src.js", [u["parts/Globals.js"],
        u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent, q = k.defined, G = k.isNumber, M = k.pick;
        k = f.Chart;
        u(f.Axis, "afterSetOptions", function () {
            var f = this.chart.options && this.chart.options.chart;
            !this.horiz && G(this.options.staticScale) && (!f.height || f.scrollablePlotArea && f.scrollablePlotArea.minHeight) && (this.staticScale = this.options.staticScale)
        });
        k.prototype.adjustHeight = function () {
            "adjustHeight" !== this.redrawTrigger && ((this.axes || []).forEach(function (f) {
                var k = f.chart, u = !!k.initiatedScale && k.options.animation,
                    E = f.options.staticScale;
                if (f.staticScale && q(f.min)) {
                    var A = M(f.unitLength, f.max + f.tickInterval - f.min) * E;
                    A = Math.max(A, E);
                    E = A - k.plotHeight;
                    1 <= Math.abs(E) && (k.plotHeight = A, k.redrawTrigger = "adjustHeight", k.setSize(void 0, k.chartHeight + E, u));
                    f.series.forEach(function (f) {
                        (f = f.sharedClipKey && k[f.sharedClipKey]) && f.attr({height: k.plotHeight})
                    })
                }
            }), this.initiatedScale = !0);
            this.redrawTrigger = null
        };
        u(k, "render", k.prototype.adjustHeight)
    });
    P(u, "mixins/tree-series.js", [u["parts/Color.js"], u["parts/Utilities.js"]],
        function (f, k) {
            var u = k.extend, q = k.isArray, G = k.isNumber, M = k.isObject, L = k.merge, C = k.pick;
            return {
                getColor: function (k, q) {
                    var A = q.index, v = q.mapOptionsToLevel, u = q.parentColor, y = q.parentColorIndex, p = q.series,
                        l = q.colors, n = q.siblings, e = p.points, d = p.chart.options.chart, g;
                    if (k) {
                        e = e[k.i];
                        k = v[k.level] || {};
                        if (v = e && k.colorByPoint) {
                            var c = e.index % (l ? l.length : d.colorCount);
                            var b = l && l[c]
                        }
                        if (!p.chart.styledMode) {
                            l = e && e.options.color;
                            d = k && k.color;
                            if (g = u) g = (g = k && k.colorVariation) && "brightness" === g.key ? f.parse(u).brighten(A /
                                n * g.to).get() : u;
                            g = C(l, d, b, g, p.color)
                        }
                        var a = C(e && e.options.colorIndex, k && k.colorIndex, c, y, q.colorIndex)
                    }
                    return {color: g, colorIndex: a}
                }, getLevelOptions: function (f) {
                    var k = null;
                    if (M(f)) {
                        k = {};
                        var A = G(f.from) ? f.from : 1;
                        var v = f.levels;
                        var B = {};
                        var y = M(f.defaults) ? f.defaults : {};
                        q(v) && (B = v.reduce(function (f, l) {
                                if (M(l) && G(l.level)) {
                                    var n = L({}, l);
                                    var e = "boolean" === typeof n.levelIsConstant ? n.levelIsConstant : y.levelIsConstant;
                                    delete n.levelIsConstant;
                                    delete n.level;
                                    l = l.level + (e ? 0 : A - 1);
                                    M(f[l]) ? u(f[l], n) : f[l] = n
                                }
                                return f
                            },
                            {}));
                        v = G(f.to) ? f.to : 1;
                        for (f = 0; f <= v; f++) k[f] = L({}, y, M(B[f]) ? B[f] : {})
                    }
                    return k
                }, setTreeValues: function v(f, k) {
                    var q = k.before, A = k.idRoot, p = k.mapIdToNode[A], l = k.points[f.i], n = l && l.options || {},
                        e = 0, d = [];
                    u(f, {
                        levelDynamic: f.level - (("boolean" === typeof k.levelIsConstant ? k.levelIsConstant : 1) ? 0 : p.level),
                        name: C(l && l.name, ""),
                        visible: A === f.id || ("boolean" === typeof k.visible ? k.visible : !1)
                    });
                    "function" === typeof q && (f = q(f, k));
                    f.children.forEach(function (g, c) {
                        var b = u({}, k);
                        u(b, {index: c, siblings: f.children.length, visible: f.visible});
                        g = v(g, b);
                        d.push(g);
                        g.visible && (e += g.val)
                    });
                    f.visible = 0 < e || f.visible;
                    q = C(n.value, e);
                    u(f, {children: d, childrenTotal: e, isLeaf: f.visible && !e, val: q});
                    return f
                }, updateRootId: function (f) {
                    if (M(f)) {
                        var k = M(f.options) ? f.options : {};
                        k = C(f.rootNode, k.rootId, "");
                        M(f.userOptions) && (f.userOptions.rootId = k);
                        f.rootNode = k
                    }
                    return k
                }
            }
        });
    P(u, "parts-gantt/Tree.js", [u["parts/Utilities.js"]], function (f) {
        var k = f.extend, u = f.isNumber, q = f.pick, G = function (f, k) {
            var u = f.reduce(function (f, k) {
                var v = q(k.parent, "");
                "undefined" === typeof f[v] &&
                (f[v] = []);
                f[v].push(k);
                return f
            }, {});
            Object.keys(u).forEach(function (f, q) {
                var v = u[f];
                "" !== f && -1 === k.indexOf(f) && (v.forEach(function (f) {
                    q[""].push(f)
                }), delete q[f])
            });
            return u
        }, M = function (f, C, H, E, A, v) {
            var B = 0, y = 0, p = v && v.after, l = v && v.before;
            C = {data: E, depth: H - 1, id: f, level: H, parent: C};
            var n, e;
            "function" === typeof l && l(C, v);
            l = (A[f] || []).map(function (d) {
                var g = M(d.id, f, H + 1, d, A, v), c = d.start;
                d = !0 === d.milestone ? c : d.end;
                n = !u(n) || c < n ? c : n;
                e = !u(e) || d > e ? d : e;
                B = B + 1 + g.descendants;
                y = Math.max(g.height + 1, y);
                return g
            });
            E && (E.start = q(E.start, n), E.end = q(E.end, e));
            k(C, {children: l, descendants: B, height: y});
            "function" === typeof p && p(C, v);
            return C
        };
        return {
            getListOfParents: G, getNode: M, getTree: function (f, k) {
                var q = f.map(function (f) {
                    return f.id
                });
                f = G(f, q);
                return M("", null, 1, null, f, k)
            }
        }
    });
    P(u, "modules/broken-axis.src.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent, q = k.extend, G = k.find, M = k.fireEvent, L = k.isArray, C = k.pick, F = f.Axis;
        k = f.Series;
        var E = function (f, k) {
            return G(k, function (k) {
                return k.from <
                    f && f < k.to
            })
        };
        q(F.prototype, {
            isInBreak: function (f, k) {
                var q = f.repeat || Infinity, v = f.from, p = f.to - f.from;
                k = k >= v ? (k - v) % q : q - (v - k) % q;
                return f.inclusive ? k <= p : k < p && 0 !== k
            }, isInAnyBreak: function (f, k) {
                var q = this.options.breaks, v = q && q.length, p;
                if (v) {
                    for (; v--;) if (this.isInBreak(q[v], f)) {
                        var l = !0;
                        p || (p = C(q[v].showPoints, !this.isXAxis))
                    }
                    var n = l && k ? l && !p : l
                }
                return n
            }
        });
        u(F, "afterInit", function () {
            "function" === typeof this.setBreaks && this.setBreaks(this.options.breaks, !1)
        });
        u(F, "afterSetTickPositions", function () {
            if (this.isBroken) {
                var f =
                    this.tickPositions, k = this.tickPositions.info, q = [], u;
                for (u = 0; u < f.length; u++) this.isInAnyBreak(f[u]) || q.push(f[u]);
                this.tickPositions = q;
                this.tickPositions.info = k
            }
        });
        u(F, "afterSetOptions", function () {
            this.isBroken && (this.options.ordinal = !1)
        });
        F.prototype.setBreaks = function (f, k) {
            function q(f) {
                var e = f, d;
                for (d = 0; d < p.breakArray.length; d++) {
                    var g = p.breakArray[d];
                    if (g.to <= f) e -= g.len; else if (g.from >= f) break; else if (p.isInBreak(g, f)) {
                        e -= f - g.from;
                        break
                    }
                }
                return e
            }

            function v(f) {
                var e;
                for (e = 0; e < p.breakArray.length; e++) {
                    var d =
                        p.breakArray[e];
                    if (d.from >= f) break; else d.to < f ? f += d.len : p.isInBreak(d, f) && (f += d.len)
                }
                return f
            }

            var p = this, l = L(f) && !!f.length;
            p.isDirty = p.isBroken !== l;
            p.isBroken = l;
            p.options.breaks = p.userOptions.breaks = f;
            p.forceRedraw = !0;
            p.series.forEach(function (f) {
                f.isDirty = !0
            });
            l || p.val2lin !== q || (delete p.val2lin, delete p.lin2val);
            l && (p.userOptions.ordinal = !1, p.val2lin = q, p.lin2val = v, p.setExtremes = function (f, e, d, g, c) {
                if (this.isBroken) {
                    for (var b, a = this.options.breaks; b = E(f, a);) f = b.to;
                    for (; b = E(e, a);) e = b.from;
                    e < f && (e =
                        f)
                }
                F.prototype.setExtremes.call(this, f, e, d, g, c)
            }, p.setAxisTranslation = function (f) {
                F.prototype.setAxisTranslation.call(this, f);
                this.unitLength = null;
                if (this.isBroken) {
                    f = p.options.breaks;
                    var e = [], d = [], g = 0, c, b = p.userMin || p.min, a = p.userMax || p.max,
                        l = C(p.pointRangePadding, 0), n;
                    f.forEach(function (d) {
                        c = d.repeat || Infinity;
                        p.isInBreak(d, b) && (b += d.to % c - b % c);
                        p.isInBreak(d, a) && (a -= a % c - d.from % c)
                    });
                    f.forEach(function (d) {
                        r = d.from;
                        for (c = d.repeat || Infinity; r - c > b;) r -= c;
                        for (; r < b;) r += c;
                        for (n = r; n < a; n += c) e.push({
                            value: n,
                            move: "in"
                        }), e.push({value: n + (d.to - d.from), move: "out", size: d.breakSize})
                    });
                    e.sort(function (a, b) {
                        return a.value === b.value ? ("in" === a.move ? 0 : 1) - ("in" === b.move ? 0 : 1) : a.value - b.value
                    });
                    var k = 0;
                    var r = b;
                    e.forEach(function (a) {
                        k += "in" === a.move ? 1 : -1;
                        1 === k && "in" === a.move && (r = a.value);
                        0 === k && (d.push({
                            from: r,
                            to: a.value,
                            len: a.value - r - (a.size || 0)
                        }), g += a.value - r - (a.size || 0))
                    });
                    p.breakArray = d;
                    p.unitLength = a - b - g + l;
                    M(p, "afterBreaks");
                    p.staticScale ? p.transA = p.staticScale : p.unitLength && (p.transA *= (a - p.min + l) / p.unitLength);
                    l && (p.minPixelPadding = p.transA * p.minPointOffset);
                    p.min = b;
                    p.max = a
                }
            });
            C(k, !0) && this.chart.redraw()
        };
        u(k, "afterGeneratePoints", function () {
            var f = this.options.connectNulls, k = this.points, q = this.xAxis, u = this.yAxis;
            if (this.isDirty) for (var p = k.length; p--;) {
                var l = k[p],
                    n = !(null === l.y && !1 === f) && (q && q.isInAnyBreak(l.x, !0) || u && u.isInAnyBreak(l.y, !0));
                l.visible = n ? !1 : !1 !== l.options.visible
            }
        });
        u(k, "afterRender", function () {
            this.drawBreaks(this.xAxis, ["x"]);
            this.drawBreaks(this.yAxis, C(this.pointArrayMap, ["y"]))
        });
        f.Series.prototype.drawBreaks = function (f, k) {
            var q = this, v = q.points, p, l, n, e;
            f && k.forEach(function (d) {
                p = f.breakArray || [];
                l = f.isXAxis ? f.min : C(q.options.threshold, f.min);
                v.forEach(function (g) {
                    e = C(g["stack" + d.toUpperCase()], g[d]);
                    p.forEach(function (c) {
                        n = !1;
                        if (l < c.from && e > c.to || l > c.from && e < c.from) n = "pointBreak"; else if (l < c.from && e > c.from && e < c.to || l > c.from && e > c.to && e < c.from) n = "pointInBreak";
                        n && M(f, n, {point: g, brk: c})
                    })
                })
            })
        };
        f.Series.prototype.gappedPath = function () {
            var k = this.currentDataGrouping, q = k && k.gapSize;
            k = this.options.gapSize;
            var u = this.points.slice(), y = u.length - 1, p = this.yAxis, l;
            if (k && 0 < y) for ("value" !== this.options.gapUnit && (k *= this.basePointRange), q && q > k && q >= this.basePointRange && (k = q), l = void 0; y--;) l && !1 !== l.visible || (l = u[y + 1]), q = u[y], !1 !== l.visible && !1 !== q.visible && (l.x - q.x > k && (l = (q.x + l.x) / 2, u.splice(y + 1, 0, {
                isNull: !0,
                x: l
            }), this.options.stacking && (l = p.stacks[this.stackKey][l] = new f.StackItem(p, p.options.stackLabels, !1, l, this.stack), l.total = 0)), l = q);
            return this.getGraphPath(u)
        }
    });
    P(u, "parts-gantt/TreeGrid.js",
        [u["parts/Globals.js"], u["mixins/tree-series.js"], u["parts/Tick.js"], u["parts-gantt/Tree.js"], u["parts/Utilities.js"]], function (f, k, u, q, G) {
            var H = G.addEvent, L = G.defined, C = G.fireEvent, F = G.extend, E = G.isNumber, A = G.isString,
                v = G.merge, B = G.pick, y = G.wrap, p = function (a) {
                    return Array.prototype.slice.call(a, 1)
                }, l = G.find, n = k.getLevelOptions, e = function (a) {
                    return G.isObject(a, !0)
                };
            f = f.Axis;
            k = function (a, b) {
                var c;
                for (c in b) if (Object.hasOwnProperty.call(b, c)) {
                    var d = b[c];
                    y(a, c, d)
                }
            };
            var d = function (a, b) {
                var c = a.collapseStart;
                a = a.collapseEnd;
                a >= b && (c -= .5);
                return {from: c, to: a, showPoints: !1}
            }, g = function (a) {
                return Object.keys(a.mapOfPosToGridNode).reduce(function (b, c) {
                    c = +c;
                    a.min <= c && a.max >= c && !a.isInAnyBreak(c) && b.push(c);
                    return b
                }, [])
            }, c = function (a, b) {
                var c = a.options.breaks || [], e = d(b, a.max);
                return c.some(function (a) {
                    return a.from === e.from && a.to === e.to
                })
            }, b = function (a, b) {
                var c = a.options.breaks || [];
                a = d(b, a.max);
                c.push(a);
                return c
            }, a = function (a, b) {
                var c = a.options.breaks || [], e = d(b, a.max);
                return c.reduce(function (a, b) {
                    b.to ===
                    e.to && b.from === e.from || a.push(b);
                    return a
                }, [])
            }, w = function (a, b) {
                var c = a.labelIcon, d = !c, e = b.renderer, h = b.xy, g = b.options, f = g.width, l = g.height,
                    k = h.x - f / 2 - g.padding;
                h = h.y - l / 2;
                var n = b.collapsed ? 90 : 180, r = b.show && E(h);
                d && (a.labelIcon = c = e.path(e.symbols[g.type](g.x, g.y, f, l)).addClass("highcharts-label-icon").add(b.group));
                r || c.attr({y: -9999});
                e.styledMode || c.attr({"stroke-width": 1, fill: B(b.color, "#666666")}).css({
                    cursor: "pointer",
                    stroke: g.lineColor,
                    strokeWidth: g.lineWidth
                });
                c[d ? "attr" : "animate"]({
                    translateX: k,
                    translateY: h, rotation: n
                })
            }, z = function (a, b, c) {
                var d = [], h = [], g = {}, f = {}, m = -1, k = "boolean" === typeof b ? b : !1;
                a = q.getTree(a, {
                    after: function (a) {
                        a = f[a.pos];
                        var b = 0, c = 0;
                        a.children.forEach(function (a) {
                            c += a.descendants + 1;
                            b = Math.max(a.height + 1, b)
                        });
                        a.descendants = c;
                        a.height = b;
                        a.collapsed && h.push(a)
                    }, before: function (a) {
                        var b = e(a.data) ? a.data : {}, c = A(b.name) ? b.name : "", h = g[a.parent];
                        h = e(h) ? f[h.pos] : null;
                        var n = function (a) {
                            return a.name === c
                        }, t;
                        k && e(h) && (t = l(h.children, n)) ? (n = t.pos, t.nodes.push(a)) : n = m++;
                        f[n] || (f[n] = t =
                            {
                                depth: h ? h.depth + 1 : 0,
                                name: c,
                                nodes: [a],
                                children: [],
                                pos: n
                            }, -1 !== n && d.push(c), e(h) && h.children.push(t));
                        A(a.id) && (g[a.id] = a);
                        !0 === b.collapsed && (t.collapsed = !0);
                        a.pos = n
                    }
                });
                f = function (a, b) {
                    var c = function (a, d, h) {
                        var g = d + (-1 === d ? 0 : b - 1), f = (g - d) / 2, m = d + f;
                        a.nodes.forEach(function (a) {
                            var b = a.data;
                            e(b) && (b.y = d + b.seriesIndex, delete b.seriesIndex);
                            a.pos = m
                        });
                        h[m] = a;
                        a.pos = m;
                        a.tickmarkOffset = f + .5;
                        a.collapseStart = g + .5;
                        a.children.forEach(function (a) {
                            c(a, g + 1, h);
                            g = a.collapseEnd - .5
                        });
                        a.collapseEnd = g + .5;
                        return h
                    };
                    return c(a["-1"],
                        -1, {})
                }(f, c);
                return {categories: d, mapOfIdToNode: g, mapOfPosToGridNode: f, collapsedNodes: h, tree: a}
            }, D = function (a) {
                a.target.axes.filter(function (a) {
                    return "treegrid" === a.options.type
                }).forEach(function (c) {
                    var d = c.options || {}, h = d.labels, g, f = d.uniqueNames, l = 0;
                    if (!c.mapOfPosToGridNode || c.series.some(function (a) {
                        return !a.hasRendered || a.isDirtyData || a.isDirty
                    })) {
                        d = c.series.reduce(function (a, b) {
                            b.visible && (b.options.data.forEach(function (b) {
                                e(b) && (b.seriesIndex = l, a.push(b))
                            }), !0 === f && l++);
                            return a
                        }, []);
                        var m =
                            z(d, f, !0 === f ? l : 1);
                        c.categories = m.categories;
                        c.mapOfPosToGridNode = m.mapOfPosToGridNode;
                        c.hasNames = !0;
                        c.tree = m.tree;
                        c.series.forEach(function (a) {
                            var b = a.options.data.map(function (a) {
                                return e(a) ? v(a) : a
                            });
                            a.visible && a.setData(b, !1)
                        });
                        c.mapOptionsToLevel = n({defaults: h, from: 1, levels: h.levels, to: c.tree.height});
                        "beforeRender" === a.type && (g = H(c, "foundExtremes", function () {
                            m.collapsedNodes.forEach(function (a) {
                                a = b(c, a);
                                c.setBreaks(a, !1)
                            });
                            g()
                        }))
                    }
                })
            };
            k(f.prototype, {
                init: function (a, b, c) {
                    var d = "treegrid" === c.type;
                    d && (H(b, "beforeRender", D), H(b, "beforeRedraw", D), c = v({
                        grid: {enabled: !0},
                        labels: {
                            align: "left",
                            levels: [{level: void 0}, {level: 1, style: {fontWeight: "bold"}}],
                            symbol: {type: "triangle", x: -5, y: -5, height: 10, width: 10, padding: 5}
                        },
                        uniqueNames: !1
                    }, c, {reversed: !0, grid: {columns: void 0}}));
                    a.apply(this, [b, c]);
                    d && (this.hasNames = !0, this.options.showLastLabel = !0)
                }, getMaxLabelDimensions: function (a) {
                    var b = this.options, c = b && b.labels;
                    b = c && E(c.indentation) ? b.labels.indentation : 0;
                    c = a.apply(this, p(arguments));
                    if ("treegrid" ===
                        this.options.type && this.mapOfPosToGridNode) {
                        var d = this.mapOfPosToGridNode[-1].height;
                        c.width += b * (d - 1)
                    }
                    return c
                }, generateTick: function (a, b) {
                    var c = e(this.mapOptionsToLevel) ? this.mapOptionsToLevel : {}, d = this.ticks, h = d[b], g;
                    if ("treegrid" === this.options.type) {
                        var f = this.mapOfPosToGridNode[b];
                        (c = c[f.depth]) && (g = {labels: c});
                        h ? (h.parameters.category = f.name, h.options = g, h.addLabel()) : d[b] = new u(this, b, null, void 0, {
                            category: f.name,
                            tickmarkOffset: f.tickmarkOffset,
                            options: g
                        })
                    } else a.apply(this, p(arguments))
                }, setTickInterval: function (a) {
                    var b =
                        this.options;
                    "treegrid" === b.type ? (this.min = B(this.userMin, b.min, this.dataMin), this.max = B(this.userMax, b.max, this.dataMax), C(this, "foundExtremes"), this.setAxisTranslation(!0), this.tickmarkOffset = .5, this.tickInterval = 1, this.tickPositions = this.mapOfPosToGridNode ? g(this) : []) : a.apply(this, p(arguments))
                }
            });
            k(u.prototype, {
                getLabelPosition: function (a, b, c, d, g, f, l, m, n) {
                    var h = B(this.options && this.options.labels, f);
                    f = this.pos;
                    var k = this.axis, t = "treegrid" === k.options.type;
                    a = a.apply(this, [b, c, d, g, h, l, m, n]);
                    t &&
                    (b = h && e(h.symbol) ? h.symbol : {}, h = h && E(h.indentation) ? h.indentation : 0, f = (f = (k = k.mapOfPosToGridNode) && k[f]) && f.depth || 1, a.x += b.width + 2 * b.padding + (f - 1) * h);
                    return a
                }, renderLabel: function (a) {
                    var b = this, d = b.pos, g = b.axis, f = b.label, l = g.mapOfPosToGridNode, k = g.options,
                        m = B(b.options && b.options.labels, k && k.labels), n = m && e(m.symbol) ? m.symbol : {},
                        r = (l = l && l[d]) && l.depth;
                    k = "treegrid" === k.type;
                    var q = !(!f || !f.element), z = -1 < g.tickPositions.indexOf(d);
                    d = g.chart.styledMode;
                    k && l && q && f.addClass("highcharts-treegrid-node-level-" +
                        r);
                    a.apply(b, p(arguments));
                    k && l && q && 0 < l.descendants && (g = c(g, l), w(b, {
                        color: !d && f.styles.color,
                        collapsed: g,
                        group: f.parentGroup,
                        options: n,
                        renderer: f.renderer,
                        show: z,
                        xy: f.xy
                    }), n = "highcharts-treegrid-node-" + (g ? "expanded" : "collapsed"), f.addClass("highcharts-treegrid-node-" + (g ? "collapsed" : "expanded")).removeClass(n), d || f.css({cursor: "pointer"}), [f, b.labelIcon].forEach(function (a) {
                        a.attachedTreeGridEvents || (H(a.element, "mouseover", function () {
                            f.addClass("highcharts-treegrid-node-active");
                            f.renderer.styledMode ||
                            f.css({textDecoration: "underline"})
                        }), H(a.element, "mouseout", function () {
                            var a = L(m.style) ? m.style : {};
                            f.removeClass("highcharts-treegrid-node-active");
                            f.renderer.styledMode || f.css({textDecoration: a.textDecoration})
                        }), H(a.element, "click", function () {
                            b.toggleCollapse()
                        }), a.attachedTreeGridEvents = !0)
                    }))
                }
            });
            F(u.prototype, {
                collapse: function (a) {
                    var c = this.axis, d = b(c, c.mapOfPosToGridNode[this.pos]);
                    c.setBreaks(d, B(a, !0))
                }, expand: function (b) {
                    var c = this.axis, d = a(c, c.mapOfPosToGridNode[this.pos]);
                    c.setBreaks(d,
                        B(b, !0))
                }, toggleCollapse: function (d) {
                    var e = this.axis;
                    var g = e.mapOfPosToGridNode[this.pos];
                    g = c(e, g) ? a(e, g) : b(e, g);
                    e.setBreaks(g, B(d, !0))
                }
            });
            f.prototype.utils = {getNode: q.getNode}
        });
    P(u, "parts-gantt/PathfinderAlgorithms.js", [u["parts/Utilities.js"]], function (f) {
        function k(f, k, q) {
            q = q || 0;
            var v = f.length - 1;
            k -= 1e-7;
            for (var p, l; q <= v;) if (p = v + q >> 1, l = k - f[p].xMin, 0 < l) q = p + 1; else if (0 > l) v = p - 1; else return p;
            return 0 < q ? q - 1 : 0
        }

        function u(f, q) {
            for (var v = k(f, q.x + 1) + 1; v--;) {
                var u;
                if (u = f[v].xMax >= q.x) u = f[v], u = q.x <= u.xMax &&
                    q.x >= u.xMin && q.y <= u.yMax && q.y >= u.yMin;
                if (u) return v
            }
            return -1
        }

        function q(f) {
            var k = [];
            if (f.length) {
                k.push("M", f[0].start.x, f[0].start.y);
                for (var q = 0; q < f.length; ++q) k.push("L", f[q].end.x, f[q].end.y)
            }
            return k
        }

        function G(f, k) {
            f.yMin = F(f.yMin, k.yMin);
            f.yMax = C(f.yMax, k.yMax);
            f.xMin = F(f.xMin, k.xMin);
            f.xMax = C(f.xMax, k.xMax)
        }

        var M = f.extend, L = f.pick, C = Math.min, F = Math.max, E = Math.abs;
        return {
            straight: function (f, k) {
                return {path: ["M", f.x, f.y, "L", k.x, k.y], obstacles: [{start: f, end: k}]}
            }, simpleConnect: M(function (f, k,
                                          B) {
                function v(b, a, c, d, e) {
                    b = {x: b.x, y: b.y};
                    b[a] = c[d || a] + (e || 0);
                    return b
                }

                function p(b, a, c) {
                    var d = E(a[c] - b[c + "Min"]) > E(a[c] - b[c + "Max"]);
                    return v(a, c, b, c + (d ? "Max" : "Min"), d ? 1 : -1)
                }

                var l = [], n = L(B.startDirectionX, E(k.x - f.x) > E(k.y - f.y)) ? "x" : "y", e = B.chartObstacles,
                    d = u(e, f);
                B = u(e, k);
                if (-1 < B) {
                    var g = e[B];
                    B = p(g, k, n);
                    g = {start: B, end: k};
                    var c = B
                } else c = k;
                -1 < d && (e = e[d], B = p(e, f, n), l.push({
                    start: f,
                    end: B
                }), B[n] >= f[n] === B[n] >= c[n] && (n = "y" === n ? "x" : "y", k = f[n] < k[n], l.push({
                    start: B,
                    end: v(B, n, e, n + (k ? "Max" : "Min"), k ? 1 : -1)
                }), n =
                    "y" === n ? "x" : "y"));
                f = l.length ? l[l.length - 1].end : f;
                B = v(f, n, c);
                l.push({start: f, end: B});
                n = v(B, "y" === n ? "x" : "y", c);
                l.push({start: B, end: n});
                l.push(g);
                return {path: q(l), obstacles: l}
            }, {requiresObstacles: !0}), fastAvoid: M(function (f, v, B) {
                function y(a, b, c) {
                    var d, e = a.x < b.x ? 1 : -1;
                    if (a.x < b.x) {
                        var h = a;
                        var f = b
                    } else h = b, f = a;
                    if (a.y < b.y) {
                        var g = a;
                        var l = b
                    } else g = b, l = a;
                    for (d = 0 > e ? C(k(r, f.x), r.length - 1) : 0; r[d] && (0 < e && r[d].xMin <= f.x || 0 > e && r[d].xMax >= h.x);) {
                        if (r[d].xMin <= f.x && r[d].xMax >= h.x && r[d].yMin <= l.y && r[d].yMax >= g.y) return c ?
                            {y: a.y, x: a.x < b.x ? r[d].xMin - 1 : r[d].xMax + 1, obstacle: r[d]} : {
                                x: a.x,
                                y: a.y < b.y ? r[d].yMin - 1 : r[d].yMax + 1,
                                obstacle: r[d]
                            };
                        d += e
                    }
                    return b
                }

                function p(a, b, c, d, e) {
                    var h = e.soft, f = e.hard, g = d ? "x" : "y", m = {x: b.x, y: b.y}, l = {x: b.x, y: b.y};
                    e = a[g + "Max"] >= h[g + "Max"];
                    h = a[g + "Min"] <= h[g + "Min"];
                    var k = a[g + "Max"] >= f[g + "Max"];
                    f = a[g + "Min"] <= f[g + "Min"];
                    var n = E(a[g + "Min"] - b[g]), t = E(a[g + "Max"] - b[g]);
                    c = 10 > E(n - t) ? b[g] < c[g] : t < n;
                    l[g] = a[g + "Min"];
                    m[g] = a[g + "Max"];
                    a = y(b, l, d)[g] !== l[g];
                    b = y(b, m, d)[g] !== m[g];
                    c = a ? b ? c : !0 : b ? !1 : c;
                    c = h ? e ? c : !0 : e ? !1 : c;
                    return f ?
                        k ? c : !0 : k ? !1 : c
                }

                function l(b, d, e) {
                    if (b.x === d.x && b.y === d.y) return [];
                    var h = e ? "x" : "y", f = B.obstacleOptions.margin;
                    var g = {soft: {xMin: a, xMax: w, yMin: z, yMax: D}, hard: B.hardBounds};
                    var k = u(r, b);
                    if (-1 < k) {
                        k = r[k];
                        g = p(k, b, d, e, g);
                        G(k, B.hardBounds);
                        var n = e ? {y: b.y, x: k[g ? "xMax" : "xMin"] + (g ? 1 : -1)} : {
                            x: b.x,
                            y: k[g ? "yMax" : "yMin"] + (g ? 1 : -1)
                        };
                        var t = u(r, n);
                        -1 < t && (t = r[t], G(t, B.hardBounds), n[h] = g ? F(k[h + "Max"] - f + 1, (t[h + "Min"] + k[h + "Max"]) / 2) : C(k[h + "Min"] + f - 1, (t[h + "Max"] + k[h + "Min"]) / 2), b.x === n.x && b.y === n.y ? (c && (n[h] = g ? F(k[h + "Max"],
                            t[h + "Max"]) + 1 : C(k[h + "Min"], t[h + "Min"]) - 1), c = !c) : c = !1);
                        b = [{start: b, end: n}]
                    } else h = y(b, {x: e ? d.x : b.x, y: e ? b.y : d.y}, e), b = [{
                        start: b,
                        end: {x: h.x, y: h.y}
                    }], h[e ? "x" : "y"] !== d[e ? "x" : "y"] && (g = p(h.obstacle, h, d, !e, g), G(h.obstacle, B.hardBounds), g = {
                        x: e ? h.x : h.obstacle[g ? "xMax" : "xMin"] + (g ? 1 : -1),
                        y: e ? h.obstacle[g ? "yMax" : "yMin"] + (g ? 1 : -1) : h.y
                    }, e = !e, b = b.concat(l({x: h.x, y: h.y}, g, e)));
                    return b = b.concat(l(b[b.length - 1].end, d, !e))
                }

                function n(a, b, c) {
                    var d = C(a.xMax - b.x, b.x - a.xMin) < C(a.yMax - b.y, b.y - a.yMin);
                    c = p(a, b, c, d, {
                        soft: B.hardBounds,
                        hard: B.hardBounds
                    });
                    return d ? {y: b.y, x: a[c ? "xMax" : "xMin"] + (c ? 1 : -1)} : {
                        x: b.x,
                        y: a[c ? "yMax" : "yMin"] + (c ? 1 : -1)
                    }
                }

                var e = L(B.startDirectionX, E(v.x - f.x) > E(v.y - f.y)), d = e ? "x" : "y", g = [], c = !1,
                    b = B.obstacleMetrics, a = C(f.x, v.x) - b.maxWidth - 10, w = F(f.x, v.x) + b.maxWidth + 10,
                    z = C(f.y, v.y) - b.maxHeight - 10, D = F(f.y, v.y) + b.maxHeight + 10, r = B.chartObstacles;
                var h = k(r, a);
                b = k(r, w);
                r = r.slice(h, b + 1);
                if (-1 < (b = u(r, v))) {
                    var t = n(r[b], v, f);
                    g.push({end: v, start: t});
                    v = t
                }
                for (; -1 < (b = u(r, v));) h = 0 > v[d] - f[d], t = {
                    x: v.x,
                    y: v.y
                }, t[d] = r[b][h ? d + "Max" : d + "Min"] +
                    (h ? 1 : -1), g.push({end: v, start: t}), v = t;
                f = l(f, v, e);
                f = f.concat(g.reverse());
                return {path: q(f), obstacles: f}
            }, {requiresObstacles: !0})
        }
    });
    P(u, "parts-gantt/ArrowSymbols.js", [u["parts/Globals.js"]], function (f) {
        f.SVGRenderer.prototype.symbols.arrow = function (f, u, q, G) {
            return ["M", f, u + G / 2, "L", f + q, u, "L", f, u + G / 2, "L", f + q, u + G]
        };
        f.SVGRenderer.prototype.symbols["arrow-half"] = function (k, u, q, G) {
            return f.SVGRenderer.prototype.symbols.arrow(k, u, q / 2, G)
        };
        f.SVGRenderer.prototype.symbols["triangle-left"] = function (f, u, q, G) {
            return ["M",
                f + q, u, "L", f, u + G / 2, "L", f + q, u + G, "Z"]
        };
        f.SVGRenderer.prototype.symbols["arrow-filled"] = f.SVGRenderer.prototype.symbols["triangle-left"];
        f.SVGRenderer.prototype.symbols["triangle-left-half"] = function (k, u, q, G) {
            return f.SVGRenderer.prototype.symbols["triangle-left"](k, u, q / 2, G)
        };
        f.SVGRenderer.prototype.symbols["arrow-filled-half"] = f.SVGRenderer.prototype.symbols["triangle-left-half"]
    });
    P(u, "parts-gantt/Pathfinder.js", [u["parts/Globals.js"], u["parts/Point.js"], u["parts/Utilities.js"], u["parts-gantt/PathfinderAlgorithms.js"]],
        function (f, k, u, q) {
            function G(c) {
                var b = c.shapeArgs;
                return b ? {
                    xMin: b.x,
                    xMax: b.x + b.width,
                    yMin: b.y,
                    yMax: b.y + b.height
                } : (b = c.graphic && c.graphic.getBBox()) ? {
                    xMin: c.plotX - b.width / 2,
                    xMax: c.plotX + b.width / 2,
                    yMin: c.plotY - b.height / 2,
                    yMax: c.plotY + b.height / 2
                } : null
            }

            function H(c) {
                for (var b = c.length, a = 0, e, f, k = [], n = function (a, b, c) {
                    c = l(c, 10);
                    var d = a.yMax + c > b.yMin - c && a.yMin - c < b.yMax + c,
                        e = a.xMax + c > b.xMin - c && a.xMin - c < b.xMax + c,
                        h = d ? a.xMin > b.xMax ? a.xMin - b.xMax : b.xMin - a.xMax : Infinity,
                        f = e ? a.yMin > b.yMax ? a.yMin - b.yMax : b.yMin - a.yMax :
                            Infinity;
                    return e && d ? c ? n(a, b, Math.floor(c / 2)) : Infinity : g(h, f)
                }; a < b; ++a) for (e = a + 1; e < b; ++e) f = n(c[a], c[e]), 80 > f && k.push(f);
                k.push(80);
                return d(Math.floor(k.sort(function (a, b) {
                    return a - b
                })[Math.floor(k.length / 10)] / 2 - 1), 1)
            }

            function L(c, b, a) {
                this.init(c, b, a)
            }

            function C(c) {
                this.init(c)
            }

            function F(c) {
                if (c.options.pathfinder || c.series.reduce(function (b, a) {
                    a.options && y(!0, a.options.connectors = a.options.connectors || {}, a.options.pathfinder);
                    return b || a.options && a.options.pathfinder
                }, !1)) y(!0, c.options.connectors =
                    c.options.connectors || {}, c.options.pathfinder), v('WARNING: Pathfinder options have been renamed. Use "chart.connectors" or "series.connectors" instead.')
            }

            "";
            var E = u.addEvent, A = u.defined, v = u.error, B = u.extend, y = u.merge, p = u.objectEach, l = u.pick,
                n = u.splat, e = f.deg2rad, d = Math.max, g = Math.min;
            B(f.defaultOptions, {
                connectors: {
                    type: "straight",
                    lineWidth: 1,
                    marker: {enabled: !1, align: "center", verticalAlign: "middle", inside: !1, lineWidth: 1},
                    startMarker: {symbol: "diamond"},
                    endMarker: {symbol: "arrow-filled"}
                }
            });
            L.prototype =
                {
                    init: function (c, b, a) {
                        this.fromPoint = c;
                        this.toPoint = b;
                        this.options = a;
                        this.chart = c.series.chart;
                        this.pathfinder = this.chart.pathfinder
                    }, renderPath: function (c, b, a) {
                        var d = this.chart, e = d.styledMode, f = d.pathfinder,
                            g = !d.options.chart.forExport && !1 !== a, h = this.graphics && this.graphics.path;
                        f.group || (f.group = d.renderer.g().addClass("highcharts-pathfinder-group").attr({zIndex: -1}).add(d.seriesGroup));
                        f.group.translate(d.plotLeft, d.plotTop);
                        h && h.renderer || (h = d.renderer.path().add(f.group), e || h.attr({opacity: 0}));
                        h.attr(b);
                        c = {d: c};
                        e || (c.opacity = 1);
                        h[g ? "animate" : "attr"](c, a);
                        this.graphics = this.graphics || {};
                        this.graphics.path = h
                    }, addMarker: function (c, b, a) {
                        var d = this.fromPoint.series.chart, f = d.pathfinder;
                        d = d.renderer;
                        var g = "start" === c ? this.fromPoint : this.toPoint, l = g.getPathfinderAnchorPoint(b);
                        if (b.enabled) {
                            a = "start" === c ? {x: a[4], y: a[5]} : {x: a[a.length - 5], y: a[a.length - 4]};
                            a = g.getRadiansToVector(a, l);
                            l = g.getMarkerVector(a, b.radius, l);
                            a = -a / e;
                            if (b.width && b.height) {
                                var h = b.width;
                                var k = b.height
                            } else h = k = 2 * b.radius;
                            this.graphics =
                                this.graphics || {};
                            l = {
                                x: l.x - h / 2,
                                y: l.y - k / 2,
                                width: h,
                                height: k,
                                rotation: a,
                                rotationOriginX: l.x,
                                rotationOriginY: l.y
                            };
                            this.graphics[c] ? this.graphics[c].animate(l) : (this.graphics[c] = d.symbol(b.symbol).addClass("highcharts-point-connecting-path-" + c + "-marker").attr(l).add(f.group), d.styledMode || this.graphics[c].attr({
                                fill: b.color || this.fromPoint.color,
                                stroke: b.lineColor,
                                "stroke-width": b.lineWidth,
                                opacity: 0
                            }).animate({opacity: 1}, g.series.options.animation))
                        }
                    }, getPath: function (c) {
                        var b = this.pathfinder, a = this.chart,
                            d = b.algorithms[c.type], e = b.chartObstacles;
                        if ("function" !== typeof d) v('"' + c.type + '" is not a Pathfinder algorithm.'); else return d.requiresObstacles && !e && (e = b.chartObstacles = b.getChartObstacles(c), a.options.connectors.algorithmMargin = c.algorithmMargin, b.chartObstacleMetrics = b.getObstacleMetrics(e)), d(this.fromPoint.getPathfinderAnchorPoint(c.startMarker), this.toPoint.getPathfinderAnchorPoint(c.endMarker), y({
                            chartObstacles: e,
                            lineObstacles: b.lineObstacles || [],
                            obstacleMetrics: b.chartObstacleMetrics,
                            hardBounds: {
                                xMin: 0,
                                xMax: a.plotWidth, yMin: 0, yMax: a.plotHeight
                            },
                            obstacleOptions: {margin: c.algorithmMargin},
                            startDirectionX: b.getAlgorithmStartDirection(c.startMarker)
                        }, c))
                    }, render: function () {
                        var c = this.fromPoint, b = c.series, a = b.chart, e = a.pathfinder,
                            f = y(a.options.connectors, b.options.connectors, c.options.connectors, this.options),
                            l = {};
                        a.styledMode || (l.stroke = f.lineColor || c.color, l["stroke-width"] = f.lineWidth, f.dashStyle && (l.dashstyle = f.dashStyle));
                        l["class"] = "highcharts-point-connecting-path highcharts-color-" + c.colorIndex;
                        f = y(l, f);
                        A(f.marker.radius) || (f.marker.radius = g(d(Math.ceil((f.algorithmMargin || 8) / 2) - 1, 1), 5));
                        c = this.getPath(f);
                        a = c.path;
                        c.obstacles && (e.lineObstacles = e.lineObstacles || [], e.lineObstacles = e.lineObstacles.concat(c.obstacles));
                        this.renderPath(a, l, b.options.animation);
                        this.addMarker("start", y(f.marker, f.startMarker), a);
                        this.addMarker("end", y(f.marker, f.endMarker), a)
                    }, destroy: function () {
                        this.graphics && (p(this.graphics, function (c) {
                            c.destroy()
                        }), delete this.graphics)
                    }
                };
            C.prototype = {
                algorithms: q, init: function (c) {
                    this.chart =
                        c;
                    this.connections = [];
                    E(c, "redraw", function () {
                        this.pathfinder.update()
                    })
                }, update: function (c) {
                    var b = this.chart, a = this, d = a.connections;
                    a.connections = [];
                    debugger
                    b.series.forEach(function (c) {
                        c.visible && !c.options.isInternal && c.points.forEach(function (c) {
                            var d, e = c.options && c.options.connect && n(c.options.connect);
                            c.visible && !1 !== c.isInside && e && e.forEach(function (e) {
                                d = b.get("string" === typeof e ? e : e.to);
                                d instanceof k && d.series.visible && d.visible && !1 !== d.isInside && a.connections.push(new L(c, d, "string" === typeof e ?
                                    {} : e))
                            })
                        })
                    });
                    for (var e = 0, f, g, h = d.length, l = a.connections.length; e < h; ++e) {
                        g = !1;
                        for (f = 0; f < l; ++f) if (d[e].fromPoint === a.connections[f].fromPoint && d[e].toPoint === a.connections[f].toPoint) {
                            a.connections[f].graphics = d[e].graphics;
                            g = !0;
                            break
                        }
                        g || d[e].destroy()
                    }
                    delete this.chartObstacles;
                    delete this.lineObstacles;
                    a.renderConnections(c)
                }, renderConnections: function (c) {
                    c ? this.chart.series.forEach(function (b) {
                        var a = function () {
                            var a = b.chart.pathfinder;
                            (a && a.connections || []).forEach(function (a) {
                                a.fromPoint && a.fromPoint.series ===
                                b && a.render()
                            });
                            b.pathfinderRemoveRenderEvent && (b.pathfinderRemoveRenderEvent(), delete b.pathfinderRemoveRenderEvent)
                        };
                        !1 === b.options.animation ? a() : b.pathfinderRemoveRenderEvent = E(b, "afterAnimate", a)
                    }) : this.connections.forEach(function (b) {
                        b.render()
                    })
                }, getChartObstacles: function (c) {
                    for (var b = [], a = this.chart.series, d = l(c.algorithmMargin, 0), e, f = 0, g = a.length; f < g; ++f) if (a[f].visible && !a[f].options.isInternal) for (var h = 0, k = a[f].points.length, n; h < k; ++h) n = a[f].points[h], n.visible && (n = G(n)) && b.push({
                        xMin: n.xMin -
                            d, xMax: n.xMax + d, yMin: n.yMin - d, yMax: n.yMax + d
                    });
                    b = b.sort(function (a, b) {
                        return a.xMin - b.xMin
                    });
                    A(c.algorithmMargin) || (e = c.algorithmMargin = H(b), b.forEach(function (a) {
                        a.xMin -= e;
                        a.xMax += e;
                        a.yMin -= e;
                        a.yMax += e
                    }));
                    return b
                }, getObstacleMetrics: function (c) {
                    for (var b = 0, a = 0, d, e, f = c.length; f--;) d = c[f].xMax - c[f].xMin, e = c[f].yMax - c[f].yMin, b < d && (b = d), a < e && (a = e);
                    return {maxHeight: a, maxWidth: b}
                }, getAlgorithmStartDirection: function (c) {
                    var b = "top" !== c.verticalAlign && "bottom" !== c.verticalAlign;
                    return "left" !== c.align &&
                    "right" !== c.align ? b ? void 0 : !1 : b ? !0 : void 0
                }
            };
            f.Connection = L;
            f.Pathfinder = C;
            B(k.prototype, {
                getPathfinderAnchorPoint: function (c) {
                    var b = G(this);
                    switch (c.align) {
                        case "right":
                            var a = "xMax";
                            break;
                        case "left":
                            a = "xMin"
                    }
                    switch (c.verticalAlign) {
                        case "top":
                            var d = "yMin";
                            break;
                        case "bottom":
                            d = "yMax"
                    }
                    return {x: a ? b[a] : (b.xMin + b.xMax) / 2, y: d ? b[d] : (b.yMin + b.yMax) / 2}
                }, getRadiansToVector: function (c, b) {
                    A(b) || (b = G(this), b = {x: (b.xMin + b.xMax) / 2, y: (b.yMin + b.yMax) / 2});
                    return Math.atan2(b.y - c.y, c.x - b.x)
                }, getMarkerVector: function (c,
                                              b, a) {
                    var d = 2 * Math.PI, e = G(this), f = e.xMax - e.xMin, g = e.yMax - e.yMin, h = Math.atan2(g, f),
                        l = !1;
                    f /= 2;
                    var k = g / 2, n = e.xMin + f;
                    e = e.yMin + k;
                    for (var p = n, q = e, m = {}, v = 1, u = 1; c < -Math.PI;) c += d;
                    for (; c > Math.PI;) c -= d;
                    d = Math.tan(c);
                    c > -h && c <= h ? (u = -1, l = !0) : c > h && c <= Math.PI - h ? u = -1 : c > Math.PI - h || c <= -(Math.PI - h) ? (v = -1, l = !0) : v = -1;
                    l ? (p += v * f, q += u * f * d) : (p += g / (2 * d) * v, q += u * k);
                    a.x !== n && (p = a.x);
                    a.y !== e && (q = a.y);
                    m.x = p + b * Math.cos(c);
                    m.y = q - b * Math.sin(c);
                    return m
                }
            });
            f.Chart.prototype.callbacks.push(function (c) {
                !1 !== c.options.connectors.enabled &&
                (F(c), this.pathfinder = new C(this), this.pathfinder.update(!0))
            })
        });
    P(u, "modules/xrange.src.js", [u["parts/Globals.js"], u["parts/Color.js"], u["parts/Point.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        var G = k.parse;
        k = q.addEvent;
        var H = q.clamp, L = q.correctFloat, C = q.defined, F = q.find, E = q.isNumber, A = q.isObject, v = q.merge,
            B = q.pick;
        q = q.seriesType;
        var y = f.seriesTypes.column, p = f.Axis, l = f.Series;
        q("xrange", "column", {
            colorByPoint: !0,
            dataLabels: {
                formatter: function () {
                    var f = this.point.partialFill;
                    A(f) && (f = f.amount);
                    if (E(f) && 0 < f) return L(100 * f) + "%"
                }, inside: !0, verticalAlign: "middle"
            },
            tooltip: {
                headerFormat: '<span style="font-size: 10px">{point.x} - {point.x2}</span><br/>',
                pointFormat: '<span style="color:{point.color}">\u25cf</span> {series.name}: <b>{point.yCategory}</b><br/>'
            },
            borderRadius: 3,
            pointRange: 0
        }, {
            type: "xrange",
            parallelArrays: ["x", "x2", "y"],
            requireSorting: !1,
            animate: f.seriesTypes.line.prototype.animate,
            cropShoulder: 1,
            getExtremesFromAll: !0,
            autoIncrement: f.noop,
            buildKDTree: f.noop,
            getColumnMetrics: function () {
                function f() {
                    e.series.forEach(function (d) {
                        var c =
                            d.xAxis;
                        d.xAxis = d.yAxis;
                        d.yAxis = c
                    })
                }

                var e = this.chart;
                f();
                var d = y.prototype.getColumnMetrics.call(this);
                f();
                return d
            },
            cropData: function (f, e, d, g) {
                e = l.prototype.cropData.call(this, this.x2Data, e, d, g);
                e.xData = f.slice(e.start, e.end);
                return e
            },
            findPointIndex: function (f) {
                var e = this.cropped, d = this.cropStart, g = this.points, c = f.id;
                if (c) var b = (b = F(g, function (a) {
                    return a.id === c
                })) ? b.index : void 0;
                "undefined" === typeof b && (b = (b = F(g, function (a) {
                    return a.x === f.x && a.x2 === f.x2 && !a.touched
                })) ? b.index : void 0);
                e && E(b) &&
                E(d) && b >= d && (b -= d);
                return b
            },
            translatePoint: function (f) {
                var e = this.xAxis, d = this.yAxis, g = this.columnMetrics, c = this.options, b = c.minPointLength || 0,
                    a = f.plotX, l = B(f.x2, f.x + (f.len || 0)), k = e.translate(l, 0, 0, 0, 1);
                l = Math.abs(k - a);
                var n = this.chart.inverted, p = B(c.borderWidth, 1) % 2 / 2, h = g.offset, t = Math.round(g.width);
                b && (b -= l, 0 > b && (b = 0), a -= b / 2, k += b / 2);
                a = Math.max(a, -10);
                k = H(k, -10, e.len + 10);
                C(f.options.pointWidth) && (h -= (Math.ceil(f.options.pointWidth) - t) / 2, t = Math.ceil(f.options.pointWidth));
                c.pointPlacement && E(f.plotY) &&
                d.categories && (f.plotY = d.translate(f.y, 0, 1, 0, 1, c.pointPlacement));
                f.shapeArgs = {
                    x: Math.floor(Math.min(a, k)) + p,
                    y: Math.floor(f.plotY + h) + p,
                    width: Math.round(Math.abs(k - a)),
                    height: t,
                    r: this.options.borderRadius
                };
                c = f.shapeArgs.x;
                b = c + f.shapeArgs.width;
                0 > c || b > e.len ? (c = H(c, 0, e.len), b = H(b, 0, e.len), k = b - c, f.dlBox = v(f.shapeArgs, {
                    x: c,
                    width: b - c,
                    centerX: k ? k / 2 : null
                })) : f.dlBox = null;
                c = f.tooltipPos;
                b = n ? 1 : 0;
                k = n ? 0 : 1;
                g = this.columnMetrics ? this.columnMetrics.offset : -g.width / 2;
                c[b] = H(c[b] + l / 2 * (e.reversed ? -1 : 1) * (n ? -1 : 1), 0, e.len -
                    1);
                c[k] = H(c[k] + (n ? -1 : 1) * g, 0, d.len - 1);
                if (g = f.partialFill) A(g) && (g = g.amount), E(g) || (g = 0), d = f.shapeArgs, f.partShapeArgs = {
                    x: d.x,
                    y: d.y,
                    width: d.width,
                    height: d.height,
                    r: this.options.borderRadius
                }, a = Math.max(Math.round(l * g + f.plotX - a), 0), f.clipRectArgs = {
                    x: e.reversed ? d.x + l - a : d.x,
                    y: d.y,
                    width: a,
                    height: d.height
                }
            },
            translate: function () {
                y.prototype.translate.apply(this, arguments);
                this.points.forEach(function (f) {
                    this.translatePoint(f)
                }, this)
            },
            drawPoint: function (f, e) {
                var d = this.options, g = this.chart.renderer, c = f.graphic,
                    b = f.shapeType, a = f.shapeArgs, l = f.partShapeArgs, k = f.clipRectArgs, n = f.partialFill,
                    p = d.stacking && !d.borderRadius, h = f.state, t = d.states[h || "normal"] || {},
                    q = "undefined" === typeof h ? "attr" : e;
                h = this.pointAttribs(f, h);
                t = B(this.chart.options.chart.animation, t.animation);
                if (f.isNull || !1 === f.visible) c && (f.graphic = c.destroy()); else {
                    if (c) c.rect[e](a); else f.graphic = c = g.g("point").addClass(f.getClassName()).add(f.group || this.group), c.rect = g[b](v(a)).addClass(f.getClassName()).addClass("highcharts-partfill-original").add(c);
                    l && (c.partRect ? (c.partRect[e](v(l)), c.partialClipRect[e](v(k))) : (c.partialClipRect = g.clipRect(k.x, k.y, k.width, k.height), c.partRect = g[b](l).addClass("highcharts-partfill-overlay").add(c).clip(c.partialClipRect)));
                    this.chart.styledMode || (c.rect[e](h, t).shadow(d.shadow, null, p), l && (A(n) || (n = {}), A(d.partialFill) && (n = v(n, d.partialFill)), f = n.fill || G(h.fill).brighten(-.3).get() || G(f.color || this.color).brighten(-.3).get(), h.fill = f, c.partRect[q](h, t).shadow(d.shadow, null, p)))
                }
            },
            drawPoints: function () {
                var f =
                    this, e = f.getAnimationVerb();
                f.points.forEach(function (d) {
                    f.drawPoint(d, e)
                })
            },
            getAnimationVerb: function () {
                return this.chart.pointCount < (this.options.animationLimit || 250) ? "animate" : "attr"
            }
        }, {
            resolveColor: function () {
                var f = this.series;
                if (f.options.colorByPoint && !this.options.color) {
                    var e = f.options.colors || f.chart.options.colors;
                    var d = this.y % (e ? e.length : f.chart.options.chart.colorCount);
                    e = e && e[d];
                    f.chart.styledMode || (this.color = e);
                    this.options.colorIndex || (this.colorIndex = d)
                } else this.color || (this.color =
                    f.color)
            }, init: function () {
                u.prototype.init.apply(this, arguments);
                this.y || (this.y = 0);
                return this
            }, setState: function () {
                u.prototype.setState.apply(this, arguments);
                this.series.drawPoint(this, this.series.getAnimationVerb())
            }, getLabelConfig: function () {
                var f = u.prototype.getLabelConfig.call(this), e = this.series.yAxis.categories;
                f.x2 = this.x2;
                f.yCategory = this.yCategory = e && e[this.y];
                return f
            }, tooltipDateKeys: ["x", "x2"], isValid: function () {
                return "number" === typeof this.x && "number" === typeof this.x2
            }
        });
        k(p, "afterGetSeriesExtremes",
            function () {
                var f = this.series, e;
                if (this.isXAxis) {
                    var d = B(this.dataMax, -Number.MAX_VALUE);
                    f.forEach(function (f) {
                        f.x2Data && f.x2Data.forEach(function (c) {
                            c > d && (d = c, e = !0)
                        })
                    });
                    e && (this.dataMax = d)
                }
            });
        ""
    });
    P(u, "parts-gantt/GanttSeries.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.isNumber, q = k.merge, G = k.pick, M = k.seriesType, L = k.splat, C = f.dateFormat,
            F = f.seriesTypes.xrange;
        M("gantt", "xrange", {
            grouping: !1, dataLabels: {enabled: !0}, tooltip: {
                headerFormat: '<span style="font-size: 10px">{series.name}</span><br/>',
                pointFormat: null, pointFormatter: function () {
                    var f = this.series, k = f.chart.tooltip, q = f.xAxis, u = f.tooltipOptions.dateTimeLabelFormats,
                        y = q.options.startOfWeek, p = f.tooltipOptions, l = p.xDateFormat;
                    f = this.options.milestone;
                    var n = "<b>" + (this.name || this.yCategory) + "</b>";
                    if (p.pointFormat) return this.tooltipFormatter(p.pointFormat);
                    l || (l = L(k.getDateFormat(q.closestPointRange, this.start, y, u))[0]);
                    k = C(l, this.start);
                    q = C(l, this.end);
                    n += "<br/>";
                    return f ? n + (k + "<br/>") : n + ("Start: " + k + "<br/>End: ") + (q + "<br/>")
                }
            }, connectors: {
                type: "simpleConnect",
                animation: {reversed: !0},
                startMarker: {enabled: !0, symbol: "arrow-filled", radius: 4, fill: "#fa0", align: "left"},
                endMarker: {enabled: !1, align: "right"}
            }
        }, {
            pointArrayMap: ["start", "end", "y"], keyboardMoveVertical: !1, translatePoint: function (f) {
                F.prototype.translatePoint.call(this, f);
                if (f.options.milestone) {
                    var k = f.shapeArgs;
                    var q = k.height;
                    f.shapeArgs = {x: k.x - q / 2, y: k.y, width: q, height: q}
                }
            }, drawPoint: function (f, k) {
                var q = this.options, A = this.chart.renderer, y = f.shapeArgs, p = f.plotY, l = f.graphic,
                    n = f.selected && "select", e =
                    q.stacking && !q.borderRadius;
                if (f.options.milestone) if (u(p) && null !== f.y && !1 !== f.visible) {
                    y = A.symbols.diamond(y.x, y.y, y.width, y.height);
                    if (l) l[k]({d: y}); else f.graphic = A.path(y).addClass(f.getClassName(), !0).add(f.group || this.group);
                    this.chart.styledMode || f.graphic.attr(this.pointAttribs(f, n)).shadow(q.shadow, null, e)
                } else l && (f.graphic = l.destroy()); else F.prototype.drawPoint.call(this, f, k)
            }, setData: f.Series.prototype.setData, setGanttPointAliases: function (f) {
                function k(k, q) {
                    "undefined" !== typeof q && (f[k] =
                        q)
                }
                k("x", G(f.start, f.x));
                k("x2", G(f.end, f.x2));
                k("partialFill", G(f.completed, f.partialFill));
                k("connect", G(f.dependency, f.connect))
            }
        }, q(F.prototype.pointClass.prototype, {
            applyOptions: function (k, u) {
                k = q(k);
                f.seriesTypes.gantt.prototype.setGanttPointAliases(k);
                return k = F.prototype.pointClass.prototype.applyOptions.call(this, k, u)
            }, isValid: function () {
                return ("number" === typeof this.start || "number" === typeof this.x) && ("number" === typeof this.end || "number" === typeof this.x2 || this.milestone)
            }
        }));
        ""
    });
    P(u, "parts-gantt/GanttChart.js",
        [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
            var u = k.isArray, q = k.merge, G = k.splat, M = f.Chart;
            f.ganttChart = function (k, C, F) {
                var E = "string" === typeof k || k.nodeName, A = C.series, v = f.getOptions(), B, y = C;
                C = arguments[E ? 1 : 0];
                u(C.xAxis) || (C.xAxis = [C.xAxis || {}, {}]);
                C.xAxis = C.xAxis.map(function (f, l) {
                    1 === l && (B = 0);
                    return q(v.xAxis, {grid: {enabled: !0}, opposite: !0, linkedTo: B}, f, {type: "datetime"})
                });
                C.yAxis = G(C.yAxis || {}).map(function (f) {
                    return q(v.yAxis, {
                        grid: {enabled: !0}, staticScale: 50, reversed: !0, type: f.categories ?
                            f.type : "treegrid"
                    }, f)
                });
                debugger
                C.series = null;
                C = q(!0, {chart: {type: "gantt"}, title: {text: null}, legend: {enabled: !1}}, C, {isGantt: !0});
                C.series = y.series = A;
                C.series.forEach(function (k) {
                    k.data.forEach(function (l) {
                        f.seriesTypes.gantt.prototype.setGanttPointAliases(l)
                    })
                });
                debugger
                return E ? new M(k, C, F) : new M(C, C)
            }
        });
    P(u, "parts/Scrollbar.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        var u = k.addEvent, q = k.correctFloat, G = k.defined, M = k.destroyObjectProperties, L = k.fireEvent,
            C = k.merge, F = k.pick, E = k.removeEvent;
        k = f.Axis;
        var A = f.defaultOptions, v = f.hasTouch, B, y = {
            height: f.isTouchDevice ? 20 : 14,
            barBorderRadius: 0,
            buttonBorderRadius: 0,
            liveRedraw: void 0,
            margin: 10,
            minWidth: 6,
            step: .2,
            zIndex: 3,
            barBackgroundColor: "#cccccc",
            barBorderWidth: 1,
            barBorderColor: "#cccccc",
            buttonArrowColor: "#333333",
            buttonBackgroundColor: "#e6e6e6",
            buttonBorderColor: "#cccccc",
            buttonBorderWidth: 1,
            rifleColor: "#333333",
            trackBackgroundColor: "#f2f2f2",
            trackBorderColor: "#f2f2f2",
            trackBorderWidth: 1
        };
        A.scrollbar = C(!0, y, A.scrollbar);
        f.swapXY = B = function (f,
                                 k) {
            var e = f.length;
            if (k) for (k = 0; k < e; k += 3) {
                var d = f[k + 1];
                f[k + 1] = f[k + 2];
                f[k + 2] = d
            }
            return f
        };
        var p = function () {
            function f(f, e, d) {
                this._events = [];
                this.from = this.chartY = this.chartX = 0;
                this.scrollbar = this.group = void 0;
                this.scrollbarButtons = [];
                this.scrollbarGroup = void 0;
                this.scrollbarLeft = 0;
                this.scrollbarRifles = void 0;
                this.scrollbarStrokeWidth = 1;
                this.to = this.size = this.scrollbarTop = 0;
                this.track = void 0;
                this.trackBorderWidth = 1;
                this.userOptions = {};
                this.y = this.x = 0;
                this.chart = d;
                this.options = e;
                this.renderer = d.renderer;
                this.init(f, e, d)
            }

            f.prototype.addEvents = function () {
                var f = this.options.inverted ? [1, 0] : [0, 1], e = this.scrollbarButtons,
                    d = this.scrollbarGroup.element, g = this.track.element, c = this.mouseDownHandler.bind(this),
                    b = this.mouseMoveHandler.bind(this), a = this.mouseUpHandler.bind(this);
                f = [[e[f[0]].element, "click", this.buttonToMinClick.bind(this)], [e[f[1]].element, "click", this.buttonToMaxClick.bind(this)], [g, "click", this.trackClick.bind(this)], [d, "mousedown", c], [d.ownerDocument, "mousemove", b], [d.ownerDocument, "mouseup",
                    a]];
                v && f.push([d, "touchstart", c], [d.ownerDocument, "touchmove", b], [d.ownerDocument, "touchend", a]);
                f.forEach(function (a) {
                    u.apply(null, a)
                });
                this._events = f
            };
            f.prototype.buttonToMaxClick = function (f) {
                var e = (this.to - this.from) * F(this.options.step, .2);
                this.updatePosition(this.from + e, this.to + e);
                L(this, "changed", {from: this.from, to: this.to, trigger: "scrollbar", DOMEvent: f})
            };
            f.prototype.buttonToMinClick = function (f) {
                var e = q(this.to - this.from) * F(this.options.step, .2);
                this.updatePosition(q(this.from - e), q(this.to -
                    e));
                L(this, "changed", {from: this.from, to: this.to, trigger: "scrollbar", DOMEvent: f})
            };
            f.prototype.cursorToScrollbarPosition = function (f) {
                var e = this.options;
                e = e.minWidth > this.calculatedWidth ? e.minWidth : 0;
                return {
                    chartX: (f.chartX - this.x - this.xOffset) / (this.barWidth - e),
                    chartY: (f.chartY - this.y - this.yOffset) / (this.barWidth - e)
                }
            };
            f.prototype.destroy = function () {
                var f = this.chart.scroller;
                this.removeEvents();
                ["track", "scrollbarRifles", "scrollbar", "scrollbarGroup", "group"].forEach(function (e) {
                    this[e] && this[e].destroy &&
                    (this[e] = this[e].destroy())
                }, this);
                f && this === f.scrollbar && (f.scrollbar = null, M(f.scrollbarButtons))
            };
            f.prototype.drawScrollbarButton = function (f) {
                var e = this.renderer, d = this.scrollbarButtons, g = this.options, c = this.size;
                var b = e.g().add(this.group);
                d.push(b);
                b = e.rect().addClass("highcharts-scrollbar-button").add(b);
                this.chart.styledMode || b.attr({
                    stroke: g.buttonBorderColor,
                    "stroke-width": g.buttonBorderWidth,
                    fill: g.buttonBackgroundColor
                });
                b.attr(b.crisp({x: -.5, y: -.5, width: c + 1, height: c + 1, r: g.buttonBorderRadius},
                    b.strokeWidth()));
                b = e.path(B(["M", c / 2 + (f ? -1 : 1), c / 2 - 3, "L", c / 2 + (f ? -1 : 1), c / 2 + 3, "L", c / 2 + (f ? 2 : -2), c / 2], g.vertical)).addClass("highcharts-scrollbar-arrow").add(d[f]);
                this.chart.styledMode || b.attr({fill: g.buttonArrowColor})
            };
            f.prototype.init = function (f, e, d) {
                this.scrollbarButtons = [];
                this.renderer = f;
                this.userOptions = e;
                this.options = C(y, e);
                this.chart = d;
                this.size = F(this.options.size, this.options.height);
                e.enabled && (this.render(), this.addEvents())
            };
            f.prototype.mouseDownHandler = function (f) {
                f = this.chart.pointer.normalize(f);
                f = this.cursorToScrollbarPosition(f);
                this.chartX = f.chartX;
                this.chartY = f.chartY;
                this.initPositions = [this.from, this.to];
                this.grabbedCenter = !0
            };
            f.prototype.mouseMoveHandler = function (f) {
                var e = this.chart.pointer.normalize(f), d = this.options.vertical ? "chartY" : "chartX",
                    g = this.initPositions || [];
                !this.grabbedCenter || f.touches && 0 === f.touches[0][d] || (e = this.cursorToScrollbarPosition(e)[d], d = this[d], d = e - d, this.hasDragged = !0, this.updatePosition(g[0] + d, g[1] + d), this.hasDragged && L(this, "changed", {
                    from: this.from, to: this.to,
                    trigger: "scrollbar", DOMType: f.type, DOMEvent: f
                }))
            };
            f.prototype.mouseUpHandler = function (f) {
                this.hasDragged && L(this, "changed", {
                    from: this.from,
                    to: this.to,
                    trigger: "scrollbar",
                    DOMType: f.type,
                    DOMEvent: f
                });
                this.grabbedCenter = this.hasDragged = this.chartX = this.chartY = null
            };
            f.prototype.position = function (f, e, d, g) {
                var c = this.options.vertical, b = 0, a = this.rendered ? "animate" : "attr";
                this.x = f;
                this.y = e + this.trackBorderWidth;
                this.width = d;
                this.xOffset = this.height = g;
                this.yOffset = b;
                c ? (this.width = this.yOffset = d = b = this.size,
                    this.xOffset = e = 0, this.barWidth = g - 2 * d, this.x = f += this.options.margin) : (this.height = this.xOffset = g = e = this.size, this.barWidth = d - 2 * g, this.y += this.options.margin);
                this.group[a]({translateX: f, translateY: this.y});
                this.track[a]({width: d, height: g});
                this.scrollbarButtons[1][a]({translateX: c ? 0 : d - e, translateY: c ? g - b : 0})
            };
            f.prototype.removeEvents = function () {
                this._events.forEach(function (f) {
                    E.apply(null, f)
                });
                this._events.length = 0
            };
            f.prototype.render = function () {
                var f = this.renderer, e = this.options, d = this.size, g = this.chart.styledMode,
                    c;
                this.group = c = f.g("scrollbar").attr({zIndex: e.zIndex, translateY: -99999}).add();
                this.track = f.rect().addClass("highcharts-scrollbar-track").attr({
                    x: 0,
                    r: e.trackBorderRadius || 0,
                    height: d,
                    width: d
                }).add(c);
                g || this.track.attr({
                    fill: e.trackBackgroundColor,
                    stroke: e.trackBorderColor,
                    "stroke-width": e.trackBorderWidth
                });
                this.trackBorderWidth = this.track.strokeWidth();
                this.track.attr({y: -this.trackBorderWidth % 2 / 2});
                this.scrollbarGroup = f.g().add(c);
                this.scrollbar = f.rect().addClass("highcharts-scrollbar-thumb").attr({
                    height: d,
                    width: d, r: e.barBorderRadius || 0
                }).add(this.scrollbarGroup);
                this.scrollbarRifles = f.path(B(["M", -3, d / 4, "L", -3, 2 * d / 3, "M", 0, d / 4, "L", 0, 2 * d / 3, "M", 3, d / 4, "L", 3, 2 * d / 3], e.vertical)).addClass("highcharts-scrollbar-rifles").add(this.scrollbarGroup);
                g || (this.scrollbar.attr({
                    fill: e.barBackgroundColor,
                    stroke: e.barBorderColor,
                    "stroke-width": e.barBorderWidth
                }), this.scrollbarRifles.attr({stroke: e.rifleColor, "stroke-width": 1}));
                this.scrollbarStrokeWidth = this.scrollbar.strokeWidth();
                this.scrollbarGroup.translate(-this.scrollbarStrokeWidth %
                    2 / 2, -this.scrollbarStrokeWidth % 2 / 2);
                this.drawScrollbarButton(0);
                this.drawScrollbarButton(1)
            };
            f.prototype.setRange = function (f, e) {
                var d = this.options, g = d.vertical, c = d.minWidth, b = this.barWidth, a,
                    k = !this.rendered || this.hasDragged || this.chart.navigator && this.chart.navigator.hasDragged ? "attr" : "animate";
                if (G(b)) {
                    f = Math.max(f, 0);
                    var l = Math.ceil(b * f);
                    this.calculatedWidth = a = q(b * Math.min(e, 1) - l);
                    a < c && (l = (b - c + a) * f, a = c);
                    c = Math.floor(l + this.xOffset + this.yOffset);
                    b = a / 2 - .5;
                    this.from = f;
                    this.to = e;
                    g ? (this.scrollbarGroup[k]({translateY: c}),
                        this.scrollbar[k]({height: a}), this.scrollbarRifles[k]({translateY: b}), this.scrollbarTop = c, this.scrollbarLeft = 0) : (this.scrollbarGroup[k]({translateX: c}), this.scrollbar[k]({width: a}), this.scrollbarRifles[k]({translateX: b}), this.scrollbarLeft = c, this.scrollbarTop = 0);
                    12 >= a ? this.scrollbarRifles.hide() : this.scrollbarRifles.show(!0);
                    !1 === d.showFull && (0 >= f && 1 <= e ? this.group.hide() : this.group.show());
                    this.rendered = !0
                }
            };
            f.prototype.trackClick = function (f) {
                var e = this.chart.pointer.normalize(f), d = this.to - this.from,
                    g = this.y + this.scrollbarTop, c = this.x + this.scrollbarLeft;
                this.options.vertical && e.chartY > g || !this.options.vertical && e.chartX > c ? this.updatePosition(this.from + d, this.to + d) : this.updatePosition(this.from - d, this.to - d);
                L(this, "changed", {from: this.from, to: this.to, trigger: "scrollbar", DOMEvent: f})
            };
            f.prototype.update = function (f) {
                this.destroy();
                this.init(this.chart.renderer, C(!0, this.options, f), this.chart)
            };
            f.prototype.updatePosition = function (f, e) {
                1 < e && (f = q(1 - q(e - f)), e = 1);
                0 > f && (e = q(e - f), f = 0);
                this.from = f;
                this.to =
                    e
            };
            return f
        }();
        f.Scrollbar || (u(k, "afterInit", function () {
            var k = this;
            k.options && k.options.scrollbar && k.options.scrollbar.enabled && (k.options.scrollbar.vertical = !k.horiz, k.options.startOnTick = k.options.endOnTick = !1, k.scrollbar = new p(k.chart.renderer, k.options.scrollbar, k.chart), u(k.scrollbar, "changed", function (l) {
                var e = Math.min(F(k.options.min, k.min), k.min, k.dataMin),
                    d = Math.max(F(k.options.max, k.max), k.max, k.dataMax) - e;
                if (k.horiz && !k.reversed || !k.horiz && k.reversed) {
                    var g = e + d * this.to;
                    e += d * this.from
                } else g =
                    e + d * (1 - this.from), e += d * (1 - this.to);
                F(this.options.liveRedraw, f.svg && !f.isTouchDevice && !this.chart.isBoosting) || "mouseup" === l.DOMType || !G(l.DOMType) ? k.setExtremes(e, g, !0, "mousemove" !== l.DOMType, l) : this.setRange(this.from, this.to)
            }))
        }), u(k, "afterRender", function () {
            var f = Math.min(F(this.options.min, this.min), this.min, F(this.dataMin, this.min)),
                k = Math.max(F(this.options.max, this.max), this.max, F(this.dataMax, this.max)), e = this.scrollbar,
                d = this.axisTitleMargin + (this.titleOffset || 0), g = this.chart.scrollbarsOffsets,
                c = this.options.margin || 0;
            e && (this.horiz ? (this.opposite || (g[1] += d), e.position(this.left, this.top + this.height + 2 + g[1] - (this.opposite ? c : 0), this.width, this.height), this.opposite || (g[1] += c), d = 1) : (this.opposite && (g[0] += d), e.position(this.left + this.width + 2 + g[0] - (this.opposite ? 0 : c), this.top, this.width, this.height), this.opposite && (g[0] += c), d = 0), g[d] += e.size + e.options.margin, isNaN(f) || isNaN(k) || !G(this.min) || !G(this.max) || this.min === this.max ? e.setRange(0, 1) : (g = (this.min - f) / (k - f), f = (this.max - f) / (k - f), this.horiz &&
            !this.reversed || !this.horiz && this.reversed ? e.setRange(g, f) : e.setRange(1 - f, 1 - g)))
        }), u(k, "afterGetOffset", function () {
            var f = this.horiz ? 2 : 1, k = this.scrollbar;
            k && (this.chart.scrollbarsOffsets = [0, 0], this.chart.axisOffset[f] += k.size + k.options.margin)
        }), f.Scrollbar = p);
        return f.Scrollbar
    });
    P(u, "parts/RangeSelector.js", [u["parts/Globals.js"], u["parts/Utilities.js"]], function (f, k) {
        function u(d) {
            this.init(d)
        }

        var q = k.addEvent, G = k.createElement, M = k.css, L = k.defined, C = k.destroyObjectProperties,
            F = k.discardElement,
            E = k.extend, A = k.fireEvent, v = k.isNumber, B = k.merge, y = k.objectEach, p = k.pick, l = k.pInt,
            n = k.splat, e = f.Axis;
        k = f.Chart;
        var d = f.defaultOptions;
        E(d, {
            rangeSelector: {
                verticalAlign: "top",
                buttonTheme: {width: 28, height: 18, padding: 2, zIndex: 7},
                floating: !1,
                x: 0,
                y: 0,
                height: void 0,
                inputPosition: {align: "right", x: 0, y: 0},
                buttonPosition: {align: "left", x: 0, y: 0},
                labelStyle: {color: "#666666"}
            }
        });
        d.lang = B(d.lang, {rangeSelectorZoom: "Zoom", rangeSelectorFrom: "From", rangeSelectorTo: "To"});
        u.prototype = {
            clickButton: function (d, c) {
                var b =
                        this.chart, a = this.buttonOptions[d], f = b.xAxis[0],
                    g = b.scroller && b.scroller.getUnionExtremes() || f || {}, k = g.dataMin, l = g.dataMax,
                    h = f && Math.round(Math.min(f.max, p(l, f.max))), t = a.type;
                g = a._range;
                var u, y = a.dataGrouping;
                if (null !== k && null !== l) {
                    b.fixedRange = g;
                    y && (this.forcedDataGrouping = !0, e.prototype.setDataGrouping.call(f || {chart: this.chart}, y, !1), this.frozenStates = a.preserveDataGrouping);
                    if ("month" === t || "year" === t) if (f) {
                        t = {range: a, max: h, chart: b, dataMin: k, dataMax: l};
                        var A = f.minFromRange.call(t);
                        v(t.newMax) &&
                        (h = t.newMax)
                    } else g = a; else if (g) A = Math.max(h - g, k), h = Math.min(A + g, l); else if ("ytd" === t) if (f) "undefined" === typeof l && (k = Number.MAX_VALUE, l = Number.MIN_VALUE, b.series.forEach(function (a) {
                        a = a.xData;
                        k = Math.min(a[0], k);
                        l = Math.max(a[a.length - 1], l)
                    }), c = !1), h = this.getYTDExtremes(l, k, b.time.useUTC), A = u = h.min, h = h.max; else {
                        this.deferredYTDClick = d;
                        return
                    } else "all" === t && f && (A = k, h = l);
                    A += a._offsetMin;
                    h += a._offsetMax;
                    this.setSelected(d);
                    if (f) f.setExtremes(A, h, p(c, 1), null, {trigger: "rangeSelectorButton", rangeSelectorButton: a});
                    else {
                        var x = n(b.options.xAxis)[0];
                        var m = x.range;
                        x.range = g;
                        var B = x.min;
                        x.min = u;
                        q(b, "load", function () {
                            x.range = m;
                            x.min = B
                        })
                    }
                }
            },
            setSelected: function (d) {
                this.selected = this.options.selected = d
            },
            defaultButtons: [{type: "month", count: 1, text: "1m"}, {
                type: "month",
                count: 3,
                text: "3m"
            }, {type: "month", count: 6, text: "6m"}, {type: "ytd", text: "YTD"}, {
                type: "year",
                count: 1,
                text: "1y"
            }, {type: "all", text: "All"}],
            init: function (d) {
                var c = this, b = d.options.rangeSelector, a = b.buttons || [].concat(c.defaultButtons), e = b.selected,
                    f = function () {
                        var a =
                            c.minInput, b = c.maxInput;
                        a && a.blur && A(a, "blur");
                        b && b.blur && A(b, "blur")
                    };
                c.chart = d;
                c.options = b;
                c.buttons = [];
                c.buttonOptions = a;
                this.unMouseDown = q(d.container, "mousedown", f);
                this.unResize = q(d, "resize", f);
                a.forEach(c.computeButtonRange);
                "undefined" !== typeof e && a[e] && this.clickButton(e, !1);
                q(d, "load", function () {
                    d.xAxis && d.xAxis[0] && q(d.xAxis[0], "setExtremes", function (a) {
                        this.max - this.min !== d.fixedRange && "rangeSelectorButton" !== a.trigger && "updatedData" !== a.trigger && c.forcedDataGrouping && !c.frozenStates &&
                        this.setDataGrouping(!1, !1)
                    })
                })
            },
            updateButtonStates: function () {
                var d = this, c = this.chart, b = c.xAxis[0], a = Math.round(b.max - b.min), e = !b.hasVisibleSeries,
                    f = c.scroller && c.scroller.getUnionExtremes() || b, k = f.dataMin, l = f.dataMax;
                c = d.getYTDExtremes(l, k, c.time.useUTC);
                var h = c.min, n = c.max, p = d.selected, q = v(p), u = d.options.allButtonsEnabled, x = d.buttons;
                d.buttonOptions.forEach(function (c, f) {
                    var g = c._range, m = c.type, t = c.count || 1, r = x[f], w = 0, v = c._offsetMax - c._offsetMin;
                    c = f === p;
                    var z = g > l - k, y = g < b.minRange, A = !1, I = !1;
                    g = g ===
                        a;
                    ("month" === m || "year" === m) && a + 36E5 >= 864E5 * {
                        month: 28,
                        year: 365
                    }[m] * t - v && a - 36E5 <= 864E5 * {
                        month: 31,
                        year: 366
                    }[m] * t + v ? g = !0 : "ytd" === m ? (g = n - h + v === a, A = !c) : "all" === m && (g = b.max - b.min >= l - k, I = !c && q && g);
                    m = !u && (z || y || I || e);
                    t = c && g || g && !q && !A || c && d.frozenStates;
                    m ? w = 3 : t && (q = !0, w = 2);
                    r.state !== w && (r.setState(w), 0 === w && p === f && d.setSelected(null))
                })
            },
            computeButtonRange: function (d) {
                var c = d.type, b = d.count || 1,
                    a = {millisecond: 1, second: 1E3, minute: 6E4, hour: 36E5, day: 864E5, week: 6048E5};
                if (a[c]) d._range = a[c] * b; else if ("month" ===
                    c || "year" === c) d._range = 864E5 * {month: 30, year: 365}[c] * b;
                d._offsetMin = p(d.offsetMin, 0);
                d._offsetMax = p(d.offsetMax, 0);
                d._range += d._offsetMax - d._offsetMin
            },
            setInputValue: function (d, c) {
                var b = this.chart.options.rangeSelector, a = this.chart.time, e = this[d + "Input"];
                L(c) && (e.previousValue = e.HCTime, e.HCTime = c);
                e.value = a.dateFormat(b.inputEditDateFormat || "%Y-%m-%d", e.HCTime);
                this[d + "DateBox"].attr({text: a.dateFormat(b.inputDateFormat || "%b %e, %Y", e.HCTime)})
            },
            showInput: function (d) {
                var c = this.inputGroup, b = this[d +
                "DateBox"];
                M(this[d + "Input"], {
                    left: c.translateX + b.x + "px",
                    top: c.translateY + "px",
                    width: b.width - 2 + "px",
                    height: b.height - 2 + "px",
                    border: "2px solid silver"
                })
            },
            hideInput: function (d) {
                M(this[d + "Input"], {border: 0, width: "1px", height: "1px"});
                this.setInputValue(d)
            },
            drawInput: function (e) {
                function c() {
                    var c = t.value, d = (n.inputDateParser || Date.parse)(c), e = a.xAxis[0],
                        f = a.scroller && a.scroller.xAxis ? a.scroller.xAxis : e, g = f.dataMin;
                    f = f.dataMax;
                    d !== t.previousValue && (t.previousValue = d, v(d) || (d = c.split("-"), d = Date.UTC(l(d[0]),
                        l(d[1]) - 1, l(d[2]))), v(d) && (a.time.useUTC || (d += 6E4 * (new Date).getTimezoneOffset()), h ? d > b.maxInput.HCTime ? d = void 0 : d < g && (d = g) : d < b.minInput.HCTime ? d = void 0 : d > f && (d = f), "undefined" !== typeof d && e.setExtremes(h ? d : e.min, h ? e.max : d, void 0, void 0, {trigger: "rangeSelectorInput"})))
                }

                var b = this, a = b.chart, g = a.renderer.style || {}, k = a.renderer, n = a.options.rangeSelector,
                    p = b.div, h = "min" === e, t, q, u = this.inputGroup;
                this[e + "Label"] = q = k.label(d.lang[h ? "rangeSelectorFrom" : "rangeSelectorTo"], this.inputGroup.offset).addClass("highcharts-range-label").attr({padding: 2}).add(u);
                u.offset += q.width + 5;
                this[e + "DateBox"] = k = k.label("", u.offset).addClass("highcharts-range-input").attr({
                    padding: 2,
                    width: n.inputBoxWidth || 90,
                    height: n.inputBoxHeight || 17,
                    "text-align": "center"
                }).on("click", function () {
                    b.showInput(e);
                    b[e + "Input"].focus()
                });
                a.styledMode || k.attr({stroke: n.inputBoxBorderColor || "#cccccc", "stroke-width": 1});
                k.add(u);
                u.offset += k.width + (h ? 10 : 0);
                this[e + "Input"] = t = G("input", {
                    name: e,
                    className: "highcharts-range-selector",
                    type: "text"
                }, {top: a.plotTop + "px"}, p);
                a.styledMode || (q.css(B(g,
                    n.labelStyle)), k.css(B({color: "#333333"}, g, n.inputStyle)), M(t, E({
                    position: "absolute",
                    border: 0,
                    width: "1px",
                    height: "1px",
                    padding: 0,
                    textAlign: "center",
                    fontSize: g.fontSize,
                    fontFamily: g.fontFamily,
                    top: "-9999em"
                }, n.inputStyle)));
                t.onfocus = function () {
                    b.showInput(e)
                };
                t.onblur = function () {
                    t === f.doc.activeElement && c();
                    b.hideInput(e);
                    t.blur()
                };
                t.onchange = c;
                t.onkeypress = function (a) {
                    13 === a.keyCode && c()
                }
            },
            getPosition: function () {
                var d = this.chart, c = d.options.rangeSelector;
                d = "top" === c.verticalAlign ? d.plotTop - d.axisOffset[0] :
                    0;
                return {buttonTop: d + c.buttonPosition.y, inputTop: d + c.inputPosition.y - 10}
            },
            getYTDExtremes: function (d, c, b) {
                var a = this.chart.time, e = new a.Date(d), f = a.get("FullYear", e);
                b = b ? a.Date.UTC(f, 0, 1) : +new a.Date(f, 0, 1);
                c = Math.max(c || 0, b);
                e = e.getTime();
                return {max: Math.min(d || e, e), min: c}
            },
            render: function (e, c) {
                var b = this, a = b.chart, f = a.renderer, g = a.container, k = a.options,
                    l = k.exporting && !1 !== k.exporting.enabled && k.navigation && k.navigation.buttonOptions,
                    h = d.lang, n = b.div, q = k.rangeSelector, u = p(k.chart.style && k.chart.style.zIndex,
                    0) + 1;
                k = q.floating;
                var v = b.buttons;
                n = b.inputGroup;
                var x = q.buttonTheme, m = q.buttonPosition, y = q.inputPosition, A = q.inputEnabled, B = x && x.states,
                    C = a.plotLeft, E = b.buttonGroup, F, H = b.options.verticalAlign, L = a.legend, M = L && L.options,
                    P = m.y, S = y.y, Y = a.hasLoaded, ca = Y ? "animate" : "attr", W = 0, aa = 0;
                if (!1 !== q.enabled) {
                    b.rendered || (b.group = F = f.g("range-selector-group").attr({zIndex: 7}).add(), b.buttonGroup = E = f.g("range-selector-buttons").add(F), b.zoomText = f.text(h.rangeSelectorZoom, 0, 15).add(E), a.styledMode || (b.zoomText.css(q.labelStyle),
                        x["stroke-width"] = p(x["stroke-width"], 0)), b.buttonOptions.forEach(function (a, c) {
                        v[c] = f.button(a.text, 0, 0, function (d) {
                            var e = a.events && a.events.click, f;
                            e && (f = e.call(a, d));
                            !1 !== f && b.clickButton(c);
                            b.isActive = !0
                        }, x, B && B.hover, B && B.select, B && B.disabled).attr({"text-align": "center"}).add(E)
                    }), !1 !== A && (b.div = n = G("div", null, {
                        position: "relative",
                        height: 0,
                        zIndex: u
                    }), g.parentNode.insertBefore(n, g), b.inputGroup = n = f.g("input-group").add(F), n.offset = 0, b.drawInput("min"), b.drawInput("max")));
                    b.zoomText[ca]({
                        x: p(C +
                            m.x, C)
                    });
                    var da = p(C + m.x, C) + b.zoomText.getBBox().width + 5;
                    b.buttonOptions.forEach(function (a, b) {
                        v[b][ca]({x: da});
                        da += v[b].width + p(q.buttonSpacing, 5)
                    });
                    C = a.plotLeft - a.spacing[3];
                    b.updateButtonStates();
                    l && this.titleCollision(a) && "top" === H && "right" === m.align && m.y + E.getBBox().height - 12 < (l.y || 0) + l.height && (W = -40);
                    g = m.x - a.spacing[3];
                    "right" === m.align ? g += W - C : "center" === m.align && (g -= C / 2);
                    E.align({y: m.y, width: E.getBBox().width, align: m.align, x: g}, !0, a.spacingBox);
                    b.group.placed = Y;
                    b.buttonGroup.placed = Y;
                    !1 !==
                    A && (W = l && this.titleCollision(a) && "top" === H && "right" === y.align && y.y - n.getBBox().height - 12 < (l.y || 0) + l.height + a.spacing[0] ? -40 : 0, "left" === y.align ? g = C : "right" === y.align && (g = -Math.max(a.axisOffset[1], -W)), n.align({
                        y: y.y,
                        width: n.getBBox().width,
                        align: y.align,
                        x: y.x + g - 2
                    }, !0, a.spacingBox), l = n.alignAttr.translateX + n.alignOptions.x - W + n.getBBox().x + 2, g = n.alignOptions.width, h = E.alignAttr.translateX + E.getBBox().x, C = E.getBBox().width + 20, (y.align === m.align || h + C > l && l + g > h && P < S + n.getBBox().height) && n.attr({
                        translateX: n.alignAttr.translateX +
                            (a.axisOffset[1] >= -W ? 0 : -W),
                        translateY: n.alignAttr.translateY + E.getBBox().height + 10
                    }), b.setInputValue("min", e), b.setInputValue("max", c), b.inputGroup.placed = Y);
                    b.group.align({verticalAlign: H}, !0, a.spacingBox);
                    e = b.group.getBBox().height + 20;
                    c = b.group.alignAttr.translateY;
                    "bottom" === H && (L = M && "bottom" === M.verticalAlign && M.enabled && !M.floating ? L.legendHeight + p(M.margin, 10) : 0, e = e + L - 20, aa = c - e - (k ? 0 : q.y) - (a.titleOffset ? a.titleOffset[2] : 0) - 10);
                    if ("top" === H) k && (aa = 0), a.titleOffset && a.titleOffset[0] && (aa = a.titleOffset[0]),
                        aa += a.margin[0] - a.spacing[0] || 0; else if ("middle" === H) if (S === P) aa = 0 > S ? c + void 0 : c; else if (S || P) aa = 0 > S || 0 > P ? aa - Math.min(S, P) : c - e + NaN;
                    b.group.translate(q.x, q.y + Math.floor(aa));
                    !1 !== A && (b.minInput.style.marginTop = b.group.translateY + "px", b.maxInput.style.marginTop = b.group.translateY + "px");
                    b.rendered = !0
                }
            },
            getHeight: function () {
                var d = this.options, c = this.group, b = d.y, a = d.buttonPosition.y, e = d.inputPosition.y;
                if (d.height) return d.height;
                d = c ? c.getBBox(!0).height + 13 + b : 0;
                c = Math.min(e, a);
                if (0 > e && 0 > a || 0 < e && 0 < a) d +=
                    Math.abs(c);
                return d
            },
            titleCollision: function (d) {
                return !(d.options.title.text || d.options.subtitle.text)
            },
            update: function (d) {
                var c = this.chart;
                B(!0, c.options.rangeSelector, d);
                this.destroy();
                this.init(c);
                c.rangeSelector.render()
            },
            destroy: function () {
                var d = this, c = d.minInput, b = d.maxInput;
                d.unMouseDown();
                d.unResize();
                C(d.buttons);
                c && (c.onfocus = c.onblur = c.onchange = null);
                b && (b.onfocus = b.onblur = b.onchange = null);
                y(d, function (a, b) {
                    a && "chart" !== b && (a.destroy ? a.destroy() : a.nodeType && F(this[b]));
                    a !== u.prototype[b] &&
                    (d[b] = null)
                }, this)
            }
        };
        e.prototype.minFromRange = function () {
            var d = this.range, c = d.type, b = this.max, a = this.chart.time, e = function (b, d) {
                var e = "year" === c ? "FullYear" : "Month", f = new a.Date(b), h = a.get(e, f);
                a.set(e, f, h + d);
                h === a.get(e, f) && a.set("Date", f, 0);
                return f.getTime() - b
            };
            if (v(d)) {
                var f = b - d;
                var k = d
            } else f = b + e(b, -d.count), this.chart && (this.chart.fixedRange = b - f);
            var l = p(this.dataMin, Number.MIN_VALUE);
            v(f) || (f = l);
            f <= l && (f = l, "undefined" === typeof k && (k = e(f, d.count)), this.newMax = Math.min(f + k, this.dataMax));
            v(b) ||
            (f = void 0);
            return f
        };
        f.RangeSelector || (q(k, "afterGetContainer", function () {
            this.options.rangeSelector.enabled && (this.rangeSelector = new u(this))
        }), q(k, "beforeRender", function () {
            var d = this.axes, c = this.rangeSelector;
            c && (v(c.deferredYTDClick) && (c.clickButton(c.deferredYTDClick), delete c.deferredYTDClick), d.forEach(function (b) {
                b.updateNames();
                b.setScale()
            }), this.getAxisMargins(), c.render(), d = c.options.verticalAlign, c.options.floating || ("bottom" === d ? this.extraBottomMargin = !0 : "middle" !== d && (this.extraTopMargin =
                !0)))
        }), q(k, "update", function (d) {
            var c = d.options.rangeSelector;
            d = this.rangeSelector;
            var b = this.extraBottomMargin, a = this.extraTopMargin;
            c && c.enabled && !L(d) && (this.options.rangeSelector.enabled = !0, this.rangeSelector = new u(this));
            this.extraTopMargin = this.extraBottomMargin = !1;
            d && (d.render(), c = c && c.verticalAlign || d.options && d.options.verticalAlign, d.options.floating || ("bottom" === c ? this.extraBottomMargin = !0 : "middle" !== c && (this.extraTopMargin = !0)), this.extraBottomMargin !== b || this.extraTopMargin !== a) && (this.isDirtyBox =
                !0)
        }), q(k, "render", function () {
            var d = this.rangeSelector;
            d && !d.options.floating && (d.render(), d = d.options.verticalAlign, "bottom" === d ? this.extraBottomMargin = !0 : "middle" !== d && (this.extraTopMargin = !0))
        }), q(k, "getMargins", function () {
            var d = this.rangeSelector;
            d && (d = d.getHeight(), this.extraTopMargin && (this.plotTop += d), this.extraBottomMargin && (this.marginBottom += d))
        }), k.prototype.callbacks.push(function (d) {
            function c() {
                b = d.xAxis[0].getExtremes();
                v(b.min) && a.render(b.min, b.max)
            }

            var b, a = d.rangeSelector;
            if (a) {
                var e =
                    q(d.xAxis[0], "afterSetExtremes", function (b) {
                        a.render(b.min, b.max)
                    });
                var f = q(d, "redraw", c);
                c()
            }
            q(d, "destroy", function () {
                a && (f(), e())
            })
        }), f.RangeSelector = u)
    });
    P(u, "parts/Navigator.js", [u["parts/Globals.js"], u["parts/Color.js"], u["parts/Scrollbar.js"], u["parts/Utilities.js"]], function (f, k, u, q) {
        k = k.parse;
        var G = q.addEvent, H = q.clamp, L = q.correctFloat, C = q.defined, F = q.destroyObjectProperties, E = q.erase,
            A = q.extend, v = q.find, B = q.isArray, y = q.isNumber, p = q.merge, l = q.pick, n = q.removeEvent,
            e = q.splat, d = f.Axis;
        q = f.Chart;
        var g = f.defaultOptions, c = f.hasTouch, b = f.isTouchDevice, a = f.Series, w = function (a) {
            for (var b = [], c = 1; c < arguments.length; c++) b[c - 1] = arguments[c];
            b = [].filter.call(b, y);
            if (b.length) return Math[a].apply(0, b)
        };
        var z = "undefined" === typeof f.seriesTypes.areaspline ? "line" : "areaspline";
        A(g, {
            navigator: {
                height: 40,
                margin: 25,
                maskInside: !0,
                handles: {
                    width: 7,
                    height: 15,
                    symbols: ["navigator-handle", "navigator-handle"],
                    enabled: !0,
                    lineWidth: 1,
                    backgroundColor: "#f2f2f2",
                    borderColor: "#999999"
                },
                maskFill: k("#6685c2").setOpacity(.3).get(),
                outlineColor: "#cccccc",
                outlineWidth: 1,
                series: {
                    type: z,
                    fillOpacity: .05,
                    lineWidth: 1,
                    compare: null,
                    dataGrouping: {
                        approximation: "average",
                        enabled: !0,
                        groupPixelWidth: 2,
                        smoothed: !0,
                        units: [["millisecond", [1, 2, 5, 10, 20, 25, 50, 100, 200, 500]], ["second", [1, 2, 5, 10, 15, 30]], ["minute", [1, 2, 5, 10, 15, 30]], ["hour", [1, 2, 3, 4, 6, 8, 12]], ["day", [1, 2, 3, 4]], ["week", [1, 2, 3]], ["month", [1, 3, 6]], ["year", null]]
                    },
                    dataLabels: {enabled: !1, zIndex: 2},
                    id: "highcharts-navigator-series",
                    className: "highcharts-navigator-series",
                    lineColor: null,
                    marker: {enabled: !1},
                    threshold: null
                },
                xAxis: {
                    overscroll: 0,
                    className: "highcharts-navigator-xaxis",
                    tickLength: 0,
                    lineWidth: 0,
                    gridLineColor: "#e6e6e6",
                    gridLineWidth: 1,
                    tickPixelInterval: 200,
                    labels: {align: "left", style: {color: "#999999"}, x: 3, y: -4},
                    crosshair: !1
                },
                yAxis: {
                    className: "highcharts-navigator-yaxis",
                    gridLineWidth: 0,
                    startOnTick: !1,
                    endOnTick: !1,
                    minPadding: .1,
                    maxPadding: .1,
                    labels: {enabled: !1},
                    crosshair: !1,
                    title: {text: null},
                    tickLength: 0,
                    tickWidth: 0
                }
            }
        });
        f.Renderer.prototype.symbols["navigator-handle"] = function (a, b, c, d, e) {
            a = e.width /
                2;
            b = Math.round(a / 3) + .5;
            e = e.height;
            return ["M", -a - 1, .5, "L", a, .5, "L", a, e + .5, "L", -a - 1, e + .5, "L", -a - 1, .5, "M", -b, 4, "L", -b, e - 3, "M", b - 1, 4, "L", b - 1, e - 3]
        };
        d.prototype.toFixedRange = function (a, b, c, d) {
            var e = this.chart && this.chart.fixedRange, f = (this.pointRange || 0) / 2;
            a = l(c, this.translate(a, !0, !this.horiz));
            b = l(d, this.translate(b, !0, !this.horiz));
            var h = e && (b - a) / e;
            C(c) || (a = L(a + f));
            C(d) || (b = L(b - f));
            .7 < h && 1.3 > h && (d ? a = b - e : b = a + e);
            y(a) && y(b) || (a = b = void 0);
            return {min: a, max: b}
        };
        var D = function () {
            function a(a) {
                this.zoomedMin =
                    this.zoomedMax = this.yAxis = this.xAxis = this.top = this.size = this.shades = this.rendered = this.range = this.outlineHeight = this.outline = this.opposite = this.navigatorSize = this.navigatorSeries = this.navigatorOptions = this.navigatorGroup = this.navigatorEnabled = this.left = this.height = this.handles = this.chart = this.baseSeries = void 0;
                this.init(a)
            }

            a.prototype.drawHandle = function (a, b, c, d) {
                var e = this.navigatorOptions.handles.height;
                this.handles[b][d](c ? {
                    translateX: Math.round(this.left + this.height / 2), translateY: Math.round(this.top +
                        parseInt(a, 10) + .5 - e)
                } : {
                    translateX: Math.round(this.left + parseInt(a, 10)),
                    translateY: Math.round(this.top + this.height / 2 - e / 2 - 1)
                })
            };
            a.prototype.drawOutline = function (a, b, c, d) {
                var e = this.navigatorOptions.maskInside, f = this.outline.strokeWidth(), h = f / 2;
                f = f % 2 / 2;
                var g = this.outlineHeight, k = this.scrollbarHeight, l = this.size, n = this.left - k, t = this.top;
                c ? (n -= h, c = t + b + f, b = t + a + f, a = ["M", n + g, t - k - f, "L", n + g, c, "L", n, c, "L", n, b, "L", n + g, b, "L", n + g, t + l + k].concat(e ? ["M", n + g, c - h, "L", n + g, b + h] : [])) : (a += n + k - f, b += n + k - f, t += h, a = ["M", n, t,
                    "L", a, t, "L", a, t + g, "L", b, t + g, "L", b, t, "L", n + l + 2 * k, t].concat(e ? ["M", a - h, t, "L", b + h, t] : []));
                this.outline[d]({d: a})
            };
            a.prototype.drawMasks = function (a, b, c, d) {
                var e = this.left, f = this.top, h = this.height;
                if (c) {
                    var g = [e, e, e];
                    var k = [f, f + a, f + b];
                    var l = [h, h, h];
                    var n = [a, b - a, this.size - b]
                } else g = [e, e + a, e + b], k = [f, f, f], l = [a, b - a, this.size - b], n = [h, h, h];
                this.shades.forEach(function (a, b) {
                    a[d]({x: g[b], y: k[b], width: l[b], height: n[b]})
                })
            };
            a.prototype.renderElements = function () {
                var a = this, b = a.navigatorOptions, c = b.maskInside, d = a.chart,
                    e = d.renderer, f, g = {cursor: d.inverted ? "ns-resize" : "ew-resize"};
                a.navigatorGroup = f = e.g("navigator").attr({zIndex: 8, visibility: "hidden"}).add();
                [!c, c, !c].forEach(function (c, h) {
                    a.shades[h] = e.rect().addClass("highcharts-navigator-mask" + (1 === h ? "-inside" : "-outside")).add(f);
                    d.styledMode || a.shades[h].attr({fill: c ? b.maskFill : "rgba(0,0,0,0)"}).css(1 === h && g)
                });
                a.outline = e.path().addClass("highcharts-navigator-outline").add(f);
                d.styledMode || a.outline.attr({"stroke-width": b.outlineWidth, stroke: b.outlineColor});
                b.handles.enabled && [0, 1].forEach(function (c) {
                    b.handles.inverted = d.inverted;
                    a.handles[c] = e.symbol(b.handles.symbols[c], -b.handles.width / 2 - 1, 0, b.handles.width, b.handles.height, b.handles);
                    a.handles[c].attr({zIndex: 7 - c}).addClass("highcharts-navigator-handle highcharts-navigator-handle-" + ["left", "right"][c]).add(f);
                    if (!d.styledMode) {
                        var h = b.handles;
                        a.handles[c].attr({
                            fill: h.backgroundColor,
                            stroke: h.borderColor,
                            "stroke-width": h.lineWidth
                        }).css(g)
                    }
                })
            };
            a.prototype.update = function (a) {
                (this.series || []).forEach(function (a) {
                    a.baseSeries &&
                    delete a.baseSeries.navigatorSeries
                });
                this.destroy();
                p(!0, this.chart.options.navigator, this.options, a);
                this.init(this.chart)
            };
            a.prototype.render = function (a, b, c, d) {
                var e = this.chart, f = this.scrollbarHeight, h, g = this.xAxis, k = g.pointRange || 0;
                var n = g.fake ? e.xAxis[0] : g;
                var p = this.navigatorEnabled, t, r = this.rendered;
                var q = e.inverted;
                var u = e.xAxis[0].minRange, v = e.xAxis[0].options.maxRange;
                if (!this.hasDragged || C(c)) {
                    a = L(a - k / 2);
                    b = L(b + k / 2);
                    if (!y(a) || !y(b)) if (r) c = 0, d = l(g.width, n.width); else return;
                    this.left = l(g.left,
                        e.plotLeft + f + (q ? e.plotWidth : 0));
                    this.size = t = h = l(g.len, (q ? e.plotHeight : e.plotWidth) - 2 * f);
                    e = q ? f : h + 2 * f;
                    c = l(c, g.toPixels(a, !0));
                    d = l(d, g.toPixels(b, !0));
                    y(c) && Infinity !== Math.abs(c) || (c = 0, d = e);
                    a = g.toValue(c, !0);
                    b = g.toValue(d, !0);
                    var w = Math.abs(L(b - a));
                    w < u ? this.grabbedLeft ? c = g.toPixels(b - u - k, !0) : this.grabbedRight && (d = g.toPixels(a + u + k, !0)) : C(v) && L(w - k) > v && (this.grabbedLeft ? c = g.toPixels(b - v - k, !0) : this.grabbedRight && (d = g.toPixels(a + v + k, !0)));
                    this.zoomedMax = H(Math.max(c, d), 0, t);
                    this.zoomedMin = H(this.fixedWidth ?
                        this.zoomedMax - this.fixedWidth : Math.min(c, d), 0, t);
                    this.range = this.zoomedMax - this.zoomedMin;
                    t = Math.round(this.zoomedMax);
                    c = Math.round(this.zoomedMin);
                    p && (this.navigatorGroup.attr({visibility: "visible"}), r = r && !this.hasDragged ? "animate" : "attr", this.drawMasks(c, t, q, r), this.drawOutline(c, t, q, r), this.navigatorOptions.handles.enabled && (this.drawHandle(c, 0, q, r), this.drawHandle(t, 1, q, r)));
                    this.scrollbar && (q ? (q = this.top - f, n = this.left - f + (p || !n.opposite ? 0 : (n.titleOffset || 0) + n.axisTitleMargin), f = h + 2 * f) : (q = this.top +
                        (p ? this.height : -f), n = this.left - f), this.scrollbar.position(n, q, e, f), this.scrollbar.setRange(this.zoomedMin / (h || 1), this.zoomedMax / (h || 1)));
                    this.rendered = !0
                }
            };
            a.prototype.addMouseEvents = function () {
                var a = this, b = a.chart, d = b.container, e = [], f, g;
                a.mouseMoveHandler = f = function (b) {
                    a.onMouseMove(b)
                };
                a.mouseUpHandler = g = function (b) {
                    a.onMouseUp(b)
                };
                e = a.getPartsEvents("mousedown");
                e.push(G(b.renderTo, "mousemove", f), G(d.ownerDocument, "mouseup", g));
                c && (e.push(G(b.renderTo, "touchmove", f), G(d.ownerDocument, "touchend",
                    g)), e.concat(a.getPartsEvents("touchstart")));
                a.eventsToUnbind = e;
                a.series && a.series[0] && e.push(G(a.series[0].xAxis, "foundExtremes", function () {
                    b.navigator.modifyNavigatorAxisExtremes()
                }))
            };
            a.prototype.getPartsEvents = function (a) {
                var b = this, c = [];
                ["shades", "handles"].forEach(function (d) {
                    b[d].forEach(function (e, f) {
                        c.push(G(e.element, a, function (a) {
                            b[d + "Mousedown"](a, f)
                        }))
                    })
                });
                return c
            };
            a.prototype.shadesMousedown = function (a, b) {
                a = this.chart.pointer.normalize(a);
                var c = this.chart, d = this.xAxis, e = this.zoomedMin,
                    f = this.left, h = this.size, g = this.range, k = a.chartX;
                c.inverted && (k = a.chartY, f = this.top);
                if (1 === b) this.grabbedCenter = k, this.fixedWidth = g, this.dragOffset = k - e; else {
                    a = k - f - g / 2;
                    if (0 === b) a = Math.max(0, a); else if (2 === b && a + g >= h) if (a = h - g, this.reversedExtremes) {
                        a -= g;
                        var l = this.getUnionExtremes().dataMin
                    } else var n = this.getUnionExtremes().dataMax;
                    a !== e && (this.fixedWidth = g, b = d.toFixedRange(a, a + g, l, n), C(b.min) && c.xAxis[0].setExtremes(Math.min(b.min, b.max), Math.max(b.min, b.max), !0, null, {trigger: "navigator"}))
                }
            };
            a.prototype.handlesMousedown =
                function (a, b) {
                    this.chart.pointer.normalize(a);
                    a = this.chart;
                    var c = a.xAxis[0], d = this.reversedExtremes;
                    0 === b ? (this.grabbedLeft = !0, this.otherHandlePos = this.zoomedMax, this.fixedExtreme = d ? c.min : c.max) : (this.grabbedRight = !0, this.otherHandlePos = this.zoomedMin, this.fixedExtreme = d ? c.max : c.min);
                    a.fixedRange = null
                };
            a.prototype.onMouseMove = function (a) {
                var c = this, d = c.chart, e = c.left, h = c.navigatorSize, g = c.range, k = c.dragOffset,
                    n = d.inverted;
                a.touches && 0 === a.touches[0].pageX || (a = d.pointer.normalize(a), d = a.chartX, n &&
                (e = c.top, d = a.chartY), c.grabbedLeft ? (c.hasDragged = !0, c.render(0, 0, d - e, c.otherHandlePos)) : c.grabbedRight ? (c.hasDragged = !0, c.render(0, 0, c.otherHandlePos, d - e)) : c.grabbedCenter && (c.hasDragged = !0, d < k ? d = k : d > h + k - g && (d = h + k - g), c.render(0, 0, d - k, d - k + g)), c.hasDragged && c.scrollbar && l(c.scrollbar.options.liveRedraw, f.svg && !b && !this.chart.isBoosting) && (a.DOMType = a.type, setTimeout(function () {
                    c.onMouseUp(a)
                }, 0)))
            };
            a.prototype.onMouseUp = function (a) {
                var b = this.chart, c = this.xAxis, d = this.scrollbar, e = a.DOMEvent || a, f = b.inverted,
                    h = this.rendered && !this.hasDragged ? "animate" : "attr", g = Math.round(this.zoomedMax),
                    k = Math.round(this.zoomedMin);
                if (this.hasDragged && (!d || !d.hasDragged) || "scrollbar" === a.trigger) {
                    d = this.getUnionExtremes();
                    if (this.zoomedMin === this.otherHandlePos) var l = this.fixedExtreme; else if (this.zoomedMax === this.otherHandlePos) var n = this.fixedExtreme;
                    this.zoomedMax === this.size && (n = this.reversedExtremes ? d.dataMin : d.dataMax);
                    0 === this.zoomedMin && (l = this.reversedExtremes ? d.dataMax : d.dataMin);
                    c = c.toFixedRange(this.zoomedMin,
                        this.zoomedMax, l, n);
                    C(c.min) && b.xAxis[0].setExtremes(Math.min(c.min, c.max), Math.max(c.min, c.max), !0, this.hasDragged ? !1 : null, {
                        trigger: "navigator",
                        triggerOp: "navigator-drag",
                        DOMEvent: e
                    })
                }
                "mousemove" !== a.DOMType && "touchmove" !== a.DOMType && (this.grabbedLeft = this.grabbedRight = this.grabbedCenter = this.fixedWidth = this.fixedExtreme = this.otherHandlePos = this.hasDragged = this.dragOffset = null);
                this.navigatorEnabled && (this.shades && this.drawMasks(k, g, f, h), this.outline && this.drawOutline(k, g, f, h), this.navigatorOptions.handles.enabled &&
                Object.keys(this.handles).length === this.handles.length && (this.drawHandle(k, 0, f, h), this.drawHandle(g, 1, f, h)))
            };
            a.prototype.removeEvents = function () {
                this.eventsToUnbind && (this.eventsToUnbind.forEach(function (a) {
                    a()
                }), this.eventsToUnbind = void 0);
                this.removeBaseSeriesEvents()
            };
            a.prototype.removeBaseSeriesEvents = function () {
                var a = this.baseSeries || [];
                this.navigatorEnabled && a[0] && (!1 !== this.navigatorOptions.adaptToUpdatedData && a.forEach(function (a) {
                    n(a, "updatedData", this.updatedDataHandler)
                }, this), a[0].xAxis &&
                n(a[0].xAxis, "foundExtremes", this.modifyBaseAxisExtremes))
            };
            a.prototype.init = function (a) {
                var b = a.options, c = b.navigator, e = c.enabled, f = b.scrollbar, h = f.enabled;
                b = e ? c.height : 0;
                var g = h ? f.height : 0;
                this.handles = [];
                this.shades = [];
                this.chart = a;
                this.setBaseSeries();
                this.height = b;
                this.scrollbarHeight = g;
                this.scrollbarEnabled = h;
                this.navigatorEnabled = e;
                this.navigatorOptions = c;
                this.scrollbarOptions = f;
                this.outlineHeight = b + g;
                this.opposite = l(c.opposite, !(e || !a.inverted));
                var k = this;
                e = k.baseSeries;
                f = a.xAxis.length;
                h = a.yAxis.length;
                var n = e && e[0] && e[0].xAxis || a.xAxis[0] || {options: {}};
                a.isDirtyBox = !0;
                k.navigatorEnabled ? (k.xAxis = new d(a, p({
                    breaks: n.options.breaks,
                    ordinal: n.options.ordinal
                }, c.xAxis, {
                    id: "navigator-x-axis",
                    yAxis: "navigator-y-axis",
                    isX: !0,
                    type: "datetime",
                    index: f,
                    isInternal: !0,
                    offset: 0,
                    keepOrdinalPadding: !0,
                    startOnTick: !1,
                    endOnTick: !1,
                    minPadding: 0,
                    maxPadding: 0,
                    zoomEnabled: !1
                }, a.inverted ? {offsets: [g, 0, -g, 0], width: b} : {
                    offsets: [0, -g, 0, g],
                    height: b
                })), k.yAxis = new d(a, p(c.yAxis, {
                    id: "navigator-y-axis", alignTicks: !1,
                    offset: 0, index: h, isInternal: !0, zoomEnabled: !1
                }, a.inverted ? {width: b} : {height: b})), e || c.series.data ? k.updateNavigatorSeries(!1) : 0 === a.series.length && (k.unbindRedraw = G(a, "beforeRedraw", function () {
                    0 < a.series.length && !k.series && (k.setBaseSeries(), k.unbindRedraw())
                })), k.reversedExtremes = a.inverted && !k.xAxis.reversed || !a.inverted && k.xAxis.reversed, k.renderElements(), k.addMouseEvents()) : k.xAxis = {
                    translate: function (b, c) {
                        var d = a.xAxis[0], e = d.getExtremes(), f = d.len - 2 * g,
                            h = w("min", d.options.min, e.dataMin);
                        d = w("max",
                            d.options.max, e.dataMax) - h;
                        return c ? b * d / f + h : f * (b - h) / d
                    }, toPixels: function (a) {
                        return this.translate(a)
                    }, toValue: function (a) {
                        return this.translate(a, !0)
                    }, toFixedRange: d.prototype.toFixedRange, fake: !0
                };
                a.options.scrollbar.enabled && (a.scrollbar = k.scrollbar = new u(a.renderer, p(a.options.scrollbar, {
                    margin: k.navigatorEnabled ? 0 : 10,
                    vertical: a.inverted
                }), a), G(k.scrollbar, "changed", function (b) {
                    var c = k.size, d = c * this.to;
                    c *= this.from;
                    k.hasDragged = k.scrollbar.hasDragged;
                    k.render(0, 0, c, d);
                    (a.options.scrollbar.liveRedraw ||
                        "mousemove" !== b.DOMType && "touchmove" !== b.DOMType) && setTimeout(function () {
                        k.onMouseUp(b)
                    })
                }));
                k.addBaseSeriesEvents();
                k.addChartEvents()
            };
            a.prototype.getUnionExtremes = function (a) {
                var b = this.chart.xAxis[0], c = this.xAxis, d = c.options, e = b.options, f;
                a && null === b.dataMin || (f = {
                    dataMin: l(d && d.min, w("min", e.min, b.dataMin, c.dataMin, c.min)),
                    dataMax: l(d && d.max, w("max", e.max, b.dataMax, c.dataMax, c.max))
                });
                return f
            };
            a.prototype.setBaseSeries = function (a, b) {
                var c = this.chart, d = this.baseSeries = [];
                a = a || c.options && c.options.navigator.baseSeries ||
                    (c.series.length ? v(c.series, function (a) {
                        return !a.options.isInternal
                    }).index : 0);
                (c.series || []).forEach(function (b, c) {
                    b.options.isInternal || !b.options.showInNavigator && (c !== a && b.options.id !== a || !1 === b.options.showInNavigator) || d.push(b)
                });
                this.xAxis && !this.xAxis.fake && this.updateNavigatorSeries(!0, b)
            };
            a.prototype.updateNavigatorSeries = function (a, b) {
                var c = this, d = c.chart, f = c.baseSeries, h, k, t = c.navigatorOptions.series, r, q = {
                    enableMouseTracking: !1,
                    index: null,
                    linkedTo: null,
                    group: "nav",
                    padXAxis: !1,
                    xAxis: "navigator-x-axis",
                    yAxis: "navigator-y-axis",
                    showInLegend: !1,
                    stacking: !1,
                    isInternal: !0,
                    states: {inactive: {opacity: 1}}
                }, u = c.series = (c.series || []).filter(function (a) {
                    var b = a.baseSeries;
                    return 0 > f.indexOf(b) ? (b && (n(b, "updatedData", c.updatedDataHandler), delete b.navigatorSeries), a.chart && a.destroy(), !1) : !0
                });
                f && f.length && f.forEach(function (a) {
                    var e = a.navigatorSeries,
                        m = A({color: a.color, visible: a.visible}, B(t) ? g.navigator.series : t);
                    e && !1 === c.navigatorOptions.adaptToUpdatedData || (q.name = "Navigator " + f.length, h = a.options || {},
                        r = h.navigatorOptions || {}, k = p(h, q, m, r), k.pointRange = l(m.pointRange, r.pointRange, g.plotOptions[k.type || "line"].pointRange), m = r.data || m.data, c.hasNavigatorData = c.hasNavigatorData || !!m, k.data = m || h.data && h.data.slice(0), e && e.options ? e.update(k, b) : (a.navigatorSeries = d.initSeries(k), a.navigatorSeries.baseSeries = a, u.push(a.navigatorSeries)))
                });
                if (t.data && (!f || !f.length) || B(t)) c.hasNavigatorData = !1, t = e(t), t.forEach(function (a, b) {
                    q.name = "Navigator " + (u.length + 1);
                    k = p(g.navigator.series, {
                        color: d.series[b] && !d.series[b].options.isInternal &&
                            d.series[b].color || d.options.colors[b] || d.options.colors[0]
                    }, q, a);
                    k.data = a.data;
                    k.data && (c.hasNavigatorData = !0, u.push(d.initSeries(k)))
                });
                a && this.addBaseSeriesEvents()
            };
            a.prototype.addBaseSeriesEvents = function () {
                var a = this, b = a.baseSeries || [];
                b[0] && b[0].xAxis && G(b[0].xAxis, "foundExtremes", this.modifyBaseAxisExtremes);
                b.forEach(function (b) {
                    G(b, "show", function () {
                        this.navigatorSeries && this.navigatorSeries.setVisible(!0, !1)
                    });
                    G(b, "hide", function () {
                        this.navigatorSeries && this.navigatorSeries.setVisible(!1,
                            !1)
                    });
                    !1 !== this.navigatorOptions.adaptToUpdatedData && b.xAxis && G(b, "updatedData", this.updatedDataHandler);
                    G(b, "remove", function () {
                        this.navigatorSeries && (E(a.series, this.navigatorSeries), C(this.navigatorSeries.options) && this.navigatorSeries.remove(!1), delete this.navigatorSeries)
                    })
                }, this)
            };
            a.prototype.getBaseSeriesMin = function (a) {
                return this.baseSeries.reduce(function (a, b) {
                    return Math.min(a, b.xData ? b.xData[0] : a)
                }, a)
            };
            a.prototype.modifyNavigatorAxisExtremes = function () {
                var a = this.xAxis, b;
                "undefined" !==
                typeof a.getExtremes && (!(b = this.getUnionExtremes(!0)) || b.dataMin === a.min && b.dataMax === a.max || (a.min = b.dataMin, a.max = b.dataMax))
            };
            a.prototype.modifyBaseAxisExtremes = function () {
                var a = this.chart.navigator, b = this.getExtremes(), c = b.dataMin, d = b.dataMax;
                b = b.max - b.min;
                var e = a.stickToMin, f = a.stickToMax, g = l(this.options.overscroll, 0), k = a.series && a.series[0],
                    n = !!this.setExtremes;
                if (!this.eventArgs || "rangeSelectorButton" !== this.eventArgs.trigger) {
                    if (e) {
                        var p = c;
                        var r = p + b
                    }
                    f && (r = d + g, e || (p = Math.max(r - b, a.getBaseSeriesMin(k &&
                    k.xData ? k.xData[0] : -Number.MAX_VALUE))));
                    n && (e || f) && y(p) && (this.min = this.userMin = p, this.max = this.userMax = r)
                }
                a.stickToMin = a.stickToMax = null
            };
            a.prototype.updatedDataHandler = function () {
                var a = this.chart.navigator, b = this.navigatorSeries, c = a.getBaseSeriesMin(this.xData[0]);
                a.stickToMax = a.reversedExtremes ? 0 === Math.round(a.zoomedMin) : Math.round(a.zoomedMax) >= Math.round(a.size);
                a.stickToMin = y(this.xAxis.min) && this.xAxis.min <= c && (!this.chart.fixedRange || !a.stickToMax);
                b && !a.hasNavigatorData && (b.options.pointStart =
                    this.xData[0], b.setData(this.options.data, !1, null, !1))
            };
            a.prototype.addChartEvents = function () {
                this.eventsToUnbind || (this.eventsToUnbind = []);
                this.eventsToUnbind.push(G(this.chart, "redraw", function () {
                    var a = this.navigator,
                        b = a && (a.baseSeries && a.baseSeries[0] && a.baseSeries[0].xAxis || a.scrollbar && this.xAxis[0]);
                    b && a.render(b.min, b.max)
                }), G(this.chart, "getMargins", function () {
                    var a = this.navigator, b = a.opposite ? "plotTop" : "marginBottom";
                    this.inverted && (b = a.opposite ? "marginRight" : "plotLeft");
                    this[b] = (this[b] ||
                        0) + (a.navigatorEnabled || !this.inverted ? a.outlineHeight : 0) + a.navigatorOptions.margin
                }))
            };
            a.prototype.destroy = function () {
                this.removeEvents();
                this.xAxis && (E(this.chart.xAxis, this.xAxis), E(this.chart.axes, this.xAxis));
                this.yAxis && (E(this.chart.yAxis, this.yAxis), E(this.chart.axes, this.yAxis));
                (this.series || []).forEach(function (a) {
                    a.destroy && a.destroy()
                });
                "series xAxis yAxis shades outline scrollbarTrack scrollbarRifles scrollbarGroup scrollbar navigatorGroup rendered".split(" ").forEach(function (a) {
                    this[a] &&
                    this[a].destroy && this[a].destroy();
                    this[a] = null
                }, this);
                [this.handles].forEach(function (a) {
                    F(a)
                }, this)
            };
            return a
        }();
        f.Navigator || (f.Navigator = D, G(d, "zoom", function (a) {
            var c = this.chart.options, d = c.chart.zoomType, e = c.chart.pinchType, f = c.navigator;
            c = c.rangeSelector;
            this.isXAxis && (f && f.enabled || c && c.enabled) && ("y" === d ? a.zoomed = !1 : (!b && "xy" === d || b && "xy" === e) && this.options.range && (d = this.previousZoom, C(a.newMin) ? this.previousZoom = [this.min, this.max] : d && (a.newMin = d[0], a.newMax = d[1], delete this.previousZoom)));
            "undefined" !== typeof a.zoomed && a.preventDefault()
        }), G(q, "beforeShowResetZoom", function () {
            var a = this.options, c = a.navigator, d = a.rangeSelector;
            if ((c && c.enabled || d && d.enabled) && (!b && "x" === a.chart.zoomType || b && "x" === a.chart.pinchType)) return !1
        }), G(q, "beforeRender", function () {
            var a = this.options;
            if (a.navigator.enabled || a.scrollbar.enabled) this.scroller = this.navigator = new D(this)
        }), G(q, "afterSetChartSize", function () {
            var a = this.legend, b = this.navigator;
            if (b) {
                var c = a && a.options;
                var d = b.xAxis;
                var e = b.yAxis;
                var f =
                    b.scrollbarHeight;
                this.inverted ? (b.left = b.opposite ? this.chartWidth - f - b.height : this.spacing[3] + f, b.top = this.plotTop + f) : (b.left = this.plotLeft + f, b.top = b.navigatorOptions.top || this.chartHeight - b.height - f - this.spacing[2] - (this.rangeSelector && this.extraBottomMargin ? this.rangeSelector.getHeight() : 0) - (c && "bottom" === c.verticalAlign && c.enabled && !c.floating ? a.legendHeight + l(c.margin, 10) : 0) - (this.titleOffset ? this.titleOffset[2] : 0));
                d && e && (this.inverted ? d.options.left = e.options.left = b.left : d.options.top = e.options.top =
                    b.top, d.setAxisSize(), e.setAxisSize())
            }
        }), G(q, "update", function (a) {
            var b = a.options.navigator || {}, c = a.options.scrollbar || {};
            this.navigator || this.scroller || !b.enabled && !c.enabled || (p(!0, this.options.navigator, b), p(!0, this.options.scrollbar, c), delete a.options.navigator, delete a.options.scrollbar)
        }), G(q, "afterUpdate", function (a) {
            this.navigator || this.scroller || !this.options.navigator.enabled && !this.options.scrollbar.enabled || (this.scroller = this.navigator = new D(this), l(a.redraw, !0) && this.redraw(a.animation))
        }),
            G(q, "afterAddSeries", function () {
                this.navigator && this.navigator.setBaseSeries(null, !1)
            }), G(a, "afterUpdate", function () {
            this.chart.navigator && !this.options.isInternal && this.chart.navigator.setBaseSeries(null, !1)
        }), q.prototype.callbacks.push(function (a) {
            var b = a.navigator;
            b && a.xAxis[0] && (a = a.xAxis[0].getExtremes(), b.render(a.min, a.max))
        }));
        f.Navigator = D;
        return f.Navigator
    });
    P(u, "masters/modules/gantt.src.js", [], function () {
    });
    P(u, "masters/highcharts-gantt.src.js", [u["masters/highcharts.src.js"]], function (f) {
        f.product =
            "Highcharts Gantt";
        return f
    });
    u["masters/highcharts-gantt.src.js"]._modules = u;
    return u["masters/highcharts-gantt.src.js"]
});
//# sourceMappingURL=highcharts-gantt.js.map