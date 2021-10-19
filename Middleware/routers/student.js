const express = require('express');
const router = express.Router();

const Student = require('../module/student');

/**
 * Kiểm tra token
 * 
 * @returns 403: Từ chối truy cập
 *          401: Token không hợp lệ
 */
checkToken = (req, res, next) => {
    const authorization = req.headers['authorization'];
    if (!authorization || authorization === '') {
        res.status(403).json({
            message: 'Bạn không có quyền thực hiện hành động này'
        })
    }
    const token = authorization.split(' ')[1];
    if (!token) return res.status(403).json({
        message: 'Bạn không có quyền thực hiện hành động này'
    })

    if (token == '6f68f34e23b7963df8d6627b609c573891720eed') {
        next()
    } else {
        res.status(401).json({
            message: 'Token không hợp lệ!'
        })
    }
}


/**
 * Lấy danh sách tất cả sinh viên
 * 
 * @returns 200: Lấy danh sách sinh viên thành công
 */
router.get('/', async (req, res, next) => {
    try {
        let result = await Student.selectAll();
        if (!result) result = [];

        for (let i = 0; i < result.length; i++) {
            result[i].ngaysinh = formatDate(result[i].ngaysinh);
        }

        res.status(200).json({
            message: 'Lấy danh sách sinh viên thành công',
            data: result
        })
    } catch (error) {
        res.status(500).json({
            message: 'Đã xảy ra lỗi hệ thống, xin hãy thử lại sau!'
        });
    }
})


/**
 * Lấy sinh viên theo mã sinh viên
 * @returns 200: Tìm thấy và trả về sinh viên 
 *          404: Khong tìm thấy
 */
router.get('/:msv', async (req, res, next) => {
    try {
        let msv = req.params.msv;

        let studentExists = await Student.has(msv);
        if (studentExists) {
            let result = await Student.selectId(msv);

            // Định dạng lại ngày tháng là dd//MM/yyyy
            result.ngaysinh = formatDate(result.ngaysinh);

            res.status(200).json({
                message: 'Đã tìm thấy sinh viên',
                data: result
            })
        } else {
            res.status(404).json({
                message: 'Không tìm thấy sinh viên, vui lòng kiểm tra lại'
            })
        }

    } catch (error) {
        res.status(500).json({
            message: 'Đã xảy ra lỗi hệ thống, xin hãy thử lại sau!'
        });
    }
})

/**
 * Kiểm tra các trường dữ liệu của sinh viên trước khi insert/update
 * 
 * @returns 400: Dữ liệu không đáp ứng yêu cầu
 */
checkStudentRequire = (req, res, next) => {
    let student = req.body;

    if (student.msv.length == 0) {
        return res.status(400).json({
            message: 'Mã sinh viên không được bỏ trống'
        })
    }

    if (student.ho.length == 0) {
        return res.status(400).json({
            message: 'Họ và tên đệm không được bỏ trống'
        })
    }

    if (student.ten.length == 0) {
        return res.status(400).json({
            message: 'Tên không được bỏ trống'
        })
    }

    if (checkDate(student.ngaysinh) === false) {
        return res.status(400).json({
            message: 'Ngày sinh không hợp lệ'
        })
    }

    let gt = student.gioitinh ?? -1;
    if (gt == -1) {
        return res.status(400).json({
            message: 'Giới tính không được bỏ trống'
        })
    }

    if (student.gioitinh < 0 || student.gioitinh > 1) {
        return res.status(400).json({
            message: 'Giới tính không hợp lệ'
        })
    }

    next();
}


/**
 * Thêm sinh viên, yêu cầu có token mới được phép thêm sinh viên
 * 
 * @returns 201: Thêm sinh viên thành công
 *          400: Mã sinh viên bị trùng, không thể thêm
 */
router.post('/', checkToken, checkStudentRequire, async (req, res, next) => {
    try {
        let student = req.body;

        let studentExists = await Student.has(student.msv);
        if (!studentExists) {
            let result = await Student.add(student);
            res.status(201).json({
                message: 'Thêm sinh viên thành công',
                data: result
            })
        } else {
            res.status(400).json({
                message: 'Mã sinh viên đã tồn tại'
            })
        }

    } catch (error) {
        console.log(error);
        res.status(500).json({
            message: 'Đã xảy ra lỗi hệ thống, xin hãy thử lại sau!'
        });
    }
})

/**
 * Cập nhật sinh viên theo mã sinh viên
 * 
 * @returns 200: Cập nhật thành công
 *          400: Dữ liệu không đáp ứng yêu cầu
 *          404: Sinh viên không tồn tại
 */
router.put('/:msv', checkToken, checkStudentRequire, async (req, res, next) => {
    try {
        let msv = req.params.msv;
        let student = req.body;

        let studentExists = await Student.has(student.msv);
        if (studentExists) {
            let result = await Student.update(msv, student);

            res.status(200).json({
                message: 'Cập nhật thông tin sinh viên thành công',
                data: result
            })
        } else {
            res.status(404).json({
                message: 'Sinh viên không tồn tại',
                data: result
            })
        }

    } catch (error) {
        console.log(error);
        res.status(500).json({
            message: 'Đã xảy ra lỗi hệ thống, xin hãy thử lại sau!'
        });
    }
})


/**
 * Xóa sinh viên theo mã sinh viên, yêu cầu có token mới được phép xóa
 * 
 * @returns 200: Xóa thành công
 *          400: Sinh viên không tồn tại nên không thể xóa
 */
router.delete('/:msv', checkToken, async (req, res, next) => {
    try {
        let msv = req.params.msv;

        let studentExists = await Student.has(msv);
        if (studentExists) {
            await Student.delete(msv);

            res.status(200).json({
                message: 'Xóa sinh viên thành công'
            })
        } else {
            res.status(404).json({
                message: 'Không có sinh viên này nên không thể xóa'
            })
        }
    } catch (error) {
        res.status(500).json({
            message: 'Đã xảy ra lỗi hệ thống, xin hãy thử lại sau!'
        });
    }
})



function formatDate(date) {
    let d = new Date(date);
    return `${d.getDate() < 10 ? '0' : ''}${d.getDate()}/${d.getMonth() + 1 < 10 ? '0' : ''}${d.getMonth() + 1}/${d.getFullYear()}`;
}

function checkDate(date) {
    let arr = date.split('/');

    let day = parseInt(arr[0]);
    let month = parseInt(arr[1]);
    let year = parseInt(arr[2]);

    if (checkMonth(month) && day > 0 && day <= lastDayOfMonth(day, month, year)) {
        return true;
    }
    return false;
}

function lastDayOfMonth(day, month, year) {
    if (month == 2) {
        if (isLeapYear(year)) return 29;
        return 28;
    } else if (month == 4 || month == 6 || month == 9 || month == 11) {
        return 30;
    }
    return 31;
}

function checkMonth(month) {
    return month > 0 && month <= 12;
}

function isLeapYear(year) {
    if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) return true;
    return false;
}

module.exports = router;