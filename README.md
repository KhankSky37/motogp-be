# motogp_backend


#1 Cách đặt tên các đầu API theo chuẩn restful naming. Tham khảo thêm https://restfulapi.net/resource-naming/
````
      Ví dụ cụ thể viết api cho quản lý người dùng
      1. Thêm mới một người dùng
         Method POST
         Naming api : $HOST/api/v1/users

      2. Tìm kiếm thông tin người dùng
         Method GET
         Naming api : $HOST/api/v1/users
      
      3. Lấy thông tin chi tiết một người dùng theo userId trong đó userId là ID của người dùng trong bảng User
         Method GET
         Naming api: $HOST/api/v1/users/{userId}
      
      4. Cập nhật thông tin cho một người dùng theo userId
         Method PUT
         Naming api: $HOST/api/v1/users/{userId}
      
      5. Xóa thông tin một người dùng theo userId
         Method DELETE
         Naming api: $HOST/api/v1/users/{userId}
````

#2 Cách đặt tên nhánh
````
      1 Nhánh master: nhánh chính chứa source code ổn định, đã được kiểm tra. 
      
      2 Nhánh develop: nhánh chính chứa source code mới nhất.
      Tất cả các thay đổi mới nhất sẽ được push/merge lên nhánh develop
      
      3 Feature branches: các nhánh hỗ trợ phục vụ quá trình phát triển
         Checkout từ: develop
         Merge vào: develop
         Đặt  tên:  [feature]/[Tên]-[Chức năng phát triển] 
         Ví dụ: feature/[KhankSky]-manage-user
         Lưu ý: Tên feature không sử dụng tiếng việt và không chứa dấu cách dấu cách
      Khi phát triển một tính năng mới, một nhánh feature sẽ được tạo từ source code mới nhất của nhánh develop, nhằm tách biệt với các tính năng đang phát triển khác. 
````


#3 Cách comment commit theo 
````
      Cấu trúc:
      <type>[scope]: <description>
      [optional body]
      -	type: Sử dụng các từ khóa sau để mô tả nội dung làm.
           feat: thêm một feature
           fix: fix bug cho hệ thống, vá lỗi trong codebase
           refactor: sửa code nhưng không fix bug cũng không thêm feature hoặc đôi khi bug cũng được fix từ việc refactor.
           docs: thêm/thay đổi document
           chore: những sửa đổi nhỏ nhặt không liên quan tới code
           style: những thay đổi không làm thay đổi ý nghĩa của code như thay đổi css/ui chẳng hạn.
           perf: code cải tiến về mặt hiệu năng xử lý
           vendor: cập nhật version cho các dependencies, packages.
      -	description: là mô tả ngắn về những gì sẽ bị sửa đổi trong commit đấy
      -	body: là mô tả dài và chi tiết hơn, cần thiết khi description chưa thể nói rõ hết được, có thể thêm phần ghi chú bằng các keyword
      
      Ví dụ: feat[home]: add pagination
````
