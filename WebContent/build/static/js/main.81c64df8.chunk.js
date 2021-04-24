(this.webpackJsonpchess=this.webpackJsonpchess||[]).push([[0],{140:function(e,n,t){},141:function(e,n,t){},439:function(e,n,t){"use strict";t.r(n);var c=t(1),o=t.n(c),a=t(130),r=t.n(a),s=(t(140),t(8)),i=(t(141),t(142),t(132)),u=t(444),l=t(3);function b(e){var n=e.children,t=e.black?"square-black":"square-white";return Object(l.jsx)("div",{className:"".concat(t," board-square"),children:n})}var d=["r","n","b","q"];function m(e){var n=e.promotion,t=n.from,c=n.to,o=n.color;return Object(l.jsx)("div",{className:"board",children:d.map((function(e,n){return Object(l.jsx)("div",{className:"promote-square",children:Object(l.jsx)("div",{className:"piece-container",onClick:function(){return S(t,c,e)},children:Object(l.jsx)("img",{src:"assets/images/".concat(e,"_").concat(o,".png"),className:"piece cursor-pointer"})})},n)}))})}var j=new i;var f=new u.a({board:j.board});console.log(document.cookie);var O,v=new WebSocket("ws://ec2-13-58-104-183.us-east-2.compute.amazonaws.com:8080/OnlineChessGame/GameEndpoint"),p="b",h=!1,g=!1,x=null,N=[];window.location.search.substr(1).split("&").forEach((function(e){"name"===(N=e.split("="))[0]&&(x=decodeURIComponent(N[1]))})),console.log("result is"+x),O=x;var w="Waiting For Player To Connect";v.onopen=function(e){v.send("username="+O)};var E=!0;function S(e,n,t){if(h){var c={from:e,to:n};t&&(c.promotion=t),j.move(c)&&(C(),v.send('{"from":"'.concat(e,'","to":"').concat(n,'","promotion":"').concat(t,'"}'),(function(){})),h=!1)}}function C(e){var n=j.game_over(),t={board:j.board(),pendingPromotion:e,isGameOver:n,turn:j.turn(),result:n?y():null,myUsername:O,opponentUsername:w,started:g};f.next(t)}function y(){var e="",n="";if(e=O,n=w,j.in_checkmate()){var t="w"===j.turn()?"BLACK":"WHITE";return console.log("My color is "+p+" and the winner is "+t),("w"==p&&"WHITE"==t||"b"==p&&"BLACK"==t)&&(console.log("sending game over info to server"),v.send("GameOver,"+e+","+n+",win")),"CHECKMATE - WINNER - ".concat(t)}if(j.in_draw()){var c="50 - MOVES - RULE";return j.in_stalemate()?c="STALEMATE":j.in_threefold_repitition()?c="REPITITION":j.insufficient_material()&&(c="INSUFFICIENT MATERIAL"),"w"==p&&v.send("GameOver,"+e+","+n+",tie"),"DRAW - ".concat(c)}return"UNKNOWN REASON"}v.onmessage=function(e){if(console.log("Recieved data from server "+e.data),"username="==e.data.substring(0,9))w=e.data.substring(9),console.log("Player Connected, opponent username is game.js is: "+w),C();else if(E)g=!0,E=!1,"w"===(p=e.data)&&(h=!0),C();else{var n=JSON.parse(e.data),t=n.from,c=n.to,o=n.promotion,a={from:t,to:c};o&&(a.promotion=o),j.move(a)&&(C(),h=!0)}},v.onclose=function(e){},v.onerror=function(e){};var I=t(445),k=t(442);function M(e){var n=e.piece,t=n.type,c=n.color,o=e.position,a=Object(I.a)({item:{type:"piece",id:"".concat(o,"_").concat(t,"_").concat(c)},collect:function(e){return{isDragging:!!e.isDragging()}}}),r=Object(s.a)(a,3),i=r[0].isDragging,u=r[1],b=r[2],d="assets/images/".concat(t,"_").concat(c,".png");return Object(l.jsxs)("div",{className:"test",children:[Object(l.jsx)(k.a,{connect:b,src:d}),Object(l.jsx)("div",{className:"piece-container",ref:u,style:{opacity:i?0:1},children:Object(l.jsx)("img",{src:d,className:"piece"})})]})}var T=t(446);function _(e){var n=e.piece,t=e.black,o=e.position,a=Object(c.useState)(null),r=Object(s.a)(a,2),i=r[0],u=r[1],d=Object(T.a)({accept:"piece",drop:function(e){!function(e,n){var t=j.moves({verbose:!0}).filter((function(e){return e.promotion}));console.table(t),t.some((function(t){return"".concat(t.from,":").concat(t.to)==="".concat(e,":").concat(n)}))&&C({from:e,to:n,color:t[0].color}),f.getValue().pendingPromotion||S(e,n)}(e.id.split("_")[0],o)}}),O=Object(s.a)(d,2)[1];return Object(c.useEffect)((function(){var e=f.subscribe((function(e){var n=e.pendingPromotion;return n&&n.to===o?u(n):u(null)}));return function(){return e.unsibscribe()}}),[]),Object(l.jsx)("div",{className:"board-square",ref:O,children:Object(l.jsx)(b,{black:t,children:i?Object(l.jsx)(m,{promotion:i}):n?Object(l.jsx)(M,{piece:n,position:o}):null})})}function A(e){var n=e.board,t=e.turn,o=Object(c.useState)([]),a=Object(s.a)(o,2),r=a[0],i=a[1];function u(e){return{x:"w"===p?e%8:Math.abs(e%8-7),y:"w"===p?Math.abs(Math.floor(e/8)-7):Math.floor(e/8)}}function b(e){var n=u(e);return(n.x+n.y)%2==0}function d(e){var n=u(e),t=n.x,c=n.y;return"".concat(["a","b","c","d","e","f","g","h"][t]).concat(c+1)}return Object(c.useEffect)((function(){i("w"===p?n.flat():n.flat().reverse())}),[n,t]),Object(l.jsx)("div",{className:"board",children:r.map((function(e,n){return Object(l.jsx)("div",{className:"square",children:Object(l.jsx)(_,{piece:e,black:b(n),position:d(n)})},n)}))})}function U(){window.location.href="http://ec2-13-58-104-183.us-east-2.compute.amazonaws.com:8080/OnlineChessGame/"}var G=function(){var e=Object(c.useState)([]),n=Object(s.a)(e,2),t=n[0],o=n[1],a=Object(c.useState)(),r=Object(s.a)(a,2),i=r[0],u=r[1],b=Object(c.useState)(),d=Object(s.a)(b,2),m=d[0],j=d[1],O=Object(c.useState)(),v=Object(s.a)(O,2),p=v[0],h=v[1],g=Object(c.useState)(),x=Object(s.a)(g,2),N=x[0],w=x[1],E=Object(c.useState)(),S=Object(s.a)(E,2),y=S[0],I=S[1];return Object(c.useEffect)((function(){C();var e=f.subscribe((function(e){o(e.board),u(e.isGameOver),j(e.result),h(e.started),w(e.myUsername),I(e.opponentUsername)}));return function(){return e.unsibscribe()}}),[]),Object(l.jsxs)("div",{children:[Object(l.jsx)("div",{children:m&&Object(l.jsx)("button",{className:"returnButton",onClick:U,children:Object(l.jsx)("h1",{children:" MAIN MENU "})})}),Object(l.jsx)("div",{className:"searching",children:p?"":"Searching For Game..."}),Object(l.jsxs)("div",{className:"container",children:[i&&Object(l.jsx)("h2",{className:"vertical-text",children:" GAME OVER"}),Object(l.jsxs)("div",{className:"board-container",children:[Object(l.jsx)("div",{className:"opponentUsernameBox",children:y}),Object(l.jsx)(A,{board:t}),Object(l.jsx)("div",{className:"myUsernameBox",children:N})]}),m&&Object(l.jsx)("p",{className:"vertical-text",children:m})]})]})},R=function(e){e&&e instanceof Function&&t.e(3).then(t.bind(null,447)).then((function(n){var t=n.getCLS,c=n.getFID,o=n.getFCP,a=n.getLCP,r=n.getTTFB;t(e),c(e),o(e),a(e),r(e)}))},F=t(443),P=t(134);r.a.render(Object(l.jsx)(o.a.StrictMode,{children:Object(l.jsx)(F.a,{backend:P.a,children:Object(l.jsx)(G,{})})}),document.getElementById("root")),R()}},[[439,1,2]]]);
//# sourceMappingURL=main.81c64df8.chunk.js.map