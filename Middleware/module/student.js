const pool = require('../database');

const db = {};


db.selectAll = () => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM sinhvien ORDER BY msv ASC", [], (err, result) => {
            if (err) return reject(err);
            return resolve(result.rows);
        })
    })
}

db.has = (msv) => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT ten FROM sinhvien WHERE msv=$1", [msv], (err, result) => {
            if (err) return reject(err);
            return resolve(result.rowCount > 0);
        })
    })
}

db.selectId = (id) => {
    return new Promise((resolve, reject) => {
        pool.query("SELECT * FROM sinhvien WHERE msv=$1", [id], (err, result) => {
            if (err) return reject(err);
            return resolve(result.rows[0]);
        })
    })
}

db.add = (s) => {
    return new Promise((resolve, reject) => {
        pool.query("INSERT INTO sinhvien (msv, ho, ten, ngaysinh, gioitinh, quequan) VALUES ($1, $2, $3, $4, $5, $6) RETURNING *",
            [s.msv, s.ho, s.ten, s.ngaysinh, s.gioitinh, s.quequan],
            (err, result) => {
                if (err) return reject(err);
                return resolve(result.rows[0]);
            })
    })
}

db.update = (msv, s) => {
    return new Promise((resolve, reject) => {
        pool.query("UPDATE sinhvien SET ho=$1, ten=$2, ngaysinh=$3, gioitinh=$4, quequan=$5 WHERE msv=$6 RETURNING *",
            [s.ho, s.ten, s.ngaysinh, s.gioitinh, s.quequan, msv],
            (err, result) => {
                if (err) return reject(err);
                return resolve(result.rows[0])
            })
    })
}

db.delete = (msv) => {
    return new Promise((resolve, reject) => {
        pool.query("DELETE FROM sinhvien where msv = $1",
            [msv],
            (err, result) => {
                if (err) return reject(err);
                return resolve(result.rows[0]);
            });
    });
}

module.exports = db;