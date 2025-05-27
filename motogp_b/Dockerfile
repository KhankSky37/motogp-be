# Sử dụng base image OpenJDK 17
FROM openjdk:17-jdk-slim

# Đặt thư mục làm việc bên trong container
WORKDIR /app

# Tạo thư mục cho uploads
RUN mkdir -p /app/uploads

# Copy file JAR đã build
COPY target/motogp_b-0.0.1-SNAPSHOT.jar app.jar

# Copy thư mục uploads, thêm dấu / để đảm bảo cấu trúc thư mục
COPY static/uploads/ /app/uploads/

# Thiết lập biến môi trường cho thư mục uploads
ENV UPLOAD_DIR=/app/uploads/

# Expose port
EXPOSE 9096

# Command để chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]