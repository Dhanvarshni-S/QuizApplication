// ═══════════════════════════════════════════════════════════════
//  CONFIG
// ═══════════════════════════════════════════════════════════════
console.log("JS LOADED");
const API = 'http://localhost:8080/api';

// ═══════════════════════════════════════════════════════════════
//  STATE
// ═══════════════════════════════════════════════════════════════
let currentUser = null;
let quizQuestions = [];
let userAnswers = [];
let currentQIndex = 0;
let timerInterval = null;
let timeLeft = 60;
let authMode = 'login';

// ═══════════════════════════════════════════════════════════════
//  PAGE NAVIGATION
// ═══════════════════════════════════════════════════════════════
function showPage(id) {
    document.querySelectorAll('.page').forEach(p => p.classList.remove('active'));
    const page = document.getElementById(id);
    if (page) page.classList.add('active');
}

function logout() {
    currentUser = null;
    document.getElementById('header-user').style.display = 'none';
    clearTimer();
    showPage('page-landing');
}

function setHeader(username, role) {
    document.getElementById('header-username').textContent = username;
    document.getElementById('header-role').textContent = '(' + role + ')';
    document.getElementById('header-user').style.display = 'flex';
}

// ═══════════════════════════════════════════════════════════════
//  AUTH — ADMIN
// ═══════════════════════════════════════════════════════════════
async function adminLogin() {
    console.log("INSIDE LOGIN");

    const username = v('admin-username');
    const password = v('admin-password');

    if (!username || !password) {
        showAlert('admin-alert', 'Enter username and password', 'error');
        return;
    }

    try {
        const res = await post('/admin/login', { username, password });

        console.log("FINAL RESPONSE:", res);

        if (res.success) {
            console.log("LOGIN SUCCESS");
            currentUser = { username, role: 'admin' };
            setHeader(username, 'Admin');
            showPage('page-admin-dashboard');

            loadAdminQuestions();
            loadAdminStats();
        } else {
            showAlert('admin-alert', res.message || 'Login failed', 'error');
        }

    } catch (e) {
        console.log("ERROR:", e);
        showAlert('admin-alert', 'Cannot connect to server.', 'error');
    }
}

// ═══════════════════════════════════════════════════════════════
//  AUTH — PARTICIPANT
// ═══════════════════════════════════════════════════════════════
function switchTab(mode) {
    authMode = mode;
    document.getElementById('tab-login').classList.toggle('active', mode === 'login');
    document.getElementById('tab-register').classList.toggle('active', mode === 'register');
    document.getElementById('p-auth-btn').textContent = mode === 'login' ? 'Login' : 'Register';
}

async function participantAuth() {
    const username = v('p-username');
    const password = v('p-password');

    if (!username || !password) {
        showAlert('participant-alert', 'Enter username and password.', 'error');
        return;
    }

    try {
        if (authMode === 'register') {
            const res = await post('/participant/register', { username, password });

            if (res.success) {
                showAlert('participant-alert', 'Registered! Please login.', 'success');
                switchTab('login');
            } else {
                showAlert('participant-alert', res.message, 'error');
            }

        } else {
            const res = await post('/participant/login', { username, password });

            if (res.success) {
                currentUser = { username, role: 'participant' };
                setHeader(username, 'Participant');
                document.getElementById('p-display-name').textContent = username;
                showPage('page-participant-dashboard');
            } else {
                showAlert('participant-alert', res.message, 'error');
            }
        }

    } catch (e) {
        showAlert('participant-alert', 'Cannot connect to server.', 'error');
    }
}

// ═══════════════════════════════════════════════════════════════
//  ADMIN — QUESTIONS
// ═══════════════════════════════════════════════════════════════
async function loadAdminQuestions() {
    try {
        const questions = await get('/admin/questions');
        const list = document.getElementById('admin-q-list');

        if (!questions.length) {
            list.innerHTML = '<p>No questions available</p>';
            return;
        }

        list.innerHTML = questions.map((q, i) => `
            <li>Q${i + 1}. ${esc(q.questionText)}</li>
        `).join('');

    } catch (e) {
        console.error("Error loading questions:", e);
    }
}

async function loadAdminStats() {
    document.getElementById('stat-users').textContent = '-';
    document.getElementById('stat-attempts').textContent = '-';
}

// ═══════════════════════════════════════════════════════════════
//  API FUNCTIONS (IMPORTANT FIX HERE)
// ═══════════════════════════════════════════════════════════════
async function post(path, data) {
    const response = await fetch(API + path, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    console.log("RAW RESPONSE:", response);

    const result = await response.json();
    console.log("PARSED RESPONSE:", result);

    return result;
}

async function get(path) {
    const response = await fetch(API + path);
    return response.json();
}

// ═══════════════════════════════════════════════════════════════
//  UTILITIES
// ═══════════════════════════════════════════════════════════════
function v(id) {
    return document.getElementById(id).value.trim();
}

function esc(s) {
    return s
        ? s.replace(/&/g, '&amp;')
             .replace(/</g, '&lt;')
             .replace(/>/g, '&gt;')
        : '';
}

function showAlert(id, msg, type) {
    const el = document.getElementById(id);
    el.textContent = msg;
    el.className = `alert show alert-${type}`;
    setTimeout(() => el.classList.remove('show'), 3000);
}

// ═══════════════════════════════════════════════════════════════
//  BUTTON FIX (IMPORTANT)
// ═══════════════════════════════════════════════════════════════
document.addEventListener("DOMContentLoaded", () => {
    const btn = document.getElementById("admin-login-btn");

    if (btn) {
        btn.addEventListener("click", () => {
            console.log("BUTTON CLICKED");
            adminLogin();
        });
    }
});