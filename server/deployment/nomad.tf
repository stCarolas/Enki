job "enki" {
  datacenters = ["dc1"]
  type = "service"

  group "listener" {
    constraint {
      attribute = "${attr.unique.hostname}"
      operator = "regexp"
      value = "app-new.*"
    }

    task "docker" {
      driver = "docker"
      env {
        GOCD_USER="slack"
        GOCD_PASSWORD="slack"
      }
      config {
        dns_servers = ["172.17.0.1"]
        image = "nexus.service.consul:5000/enki:[[.version]]"
        port_map {
          http = 8080
        }
      }

      service {
        name = "enki"
        tags = ["ui"]
      }

      resources {
        cpu    = 256
        memory = 512
        network {
          mbits = 1
          port "http" {
            static = 27005
          }
        }
      }

    }
  }
}
