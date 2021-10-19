const { Pool } = require("pg");

const pool = new Pool({
    host: 'localhost',
    database: "HTPT",
    user: "postgres",
    password: "123456",
    port: 5432
});

pool.on('error', (err) => {
	console.log("Error: " + err);
	process.exit(-1);
})


module.exports = pool;
