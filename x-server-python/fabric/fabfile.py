#/usr/bin/env python
# -*- coding: utf-8 -*-
import sys
from datetime import datetime
from fabric.api import *
from fabric.contrib.console import confirm


#测试服务器host1
testsvr='root@192.168.1.43:22'
#正式服务器host2
deploysvr='tiancaiw@115.159.125.75:22'
deploysvrfade='tiancaiw@115.158.124.75:22'



env.hosts=[testsvr,deploysvr]

#roless
env.roledefs={'testsvr':[testsvr],'deploysvr':[deploysvr]}
env.passwords={testsvr:"Nicajonh007",deploysvr:"Nicajonh007"}

api_server=['appstore_api','stat_api','updatesys_api']

remote_dist_dir=0#测试服务器网站地址
remote_sdist_dir=0#正式服务器网站地址


sudouser="root"#sudo执行用户

def pack():
    '定义一个打包任务'
    #打一个tar包
    tar_files={'*.py','appstore_api/*','conf/*','stat_api/*','updatesys_api/*','*.txt'}
    local('rm -rf x-python-svr.tar.gz')
    local('tar -czvf x-python-svr.tar.gz --exclude=\'*.tar.gz\' --exclude=\'fabfile.py\' --exclude=\'*.pyc\' --exclude=\'.git/*\' --exclude=\'.idea/*\' %s' % ' '.join(tar_files))


#定义一个TestServer部署任务
@roles('testsvr')
def deploytest():
    'TestServer部署任务'
    #远程服务器的临时文件
    global remote_dist_dir
    remote_tmp_tar = '/tmp/x-python-svr.tar.gz'
    remote_dist_link = '/var/www'
    tag = datetime.now().strftime('%y.%m.%d.%H.%M.%S')
    if not remote_dist_link:
        run('touch %s',remote_dist_link)
    # delete old remote_dir
    if remote_dist_dir:
        run('rm -rf %s' % remote_dist_dir)
    run('rm -f %s' % remote_tmp_tar)
    # 解压:
    remote_dist_dir = '/var/www.x-python-svr.com@%s' % tag
    put('x-python-svr.tar.gz', remote_tmp_tar)
    run('mkdir %s' % remote_dist_dir)
    with cd(remote_dist_dir):
        run('tar -xzvf %s' % remote_tmp_tar)
        # 设置新目录的www-data权限
    run('chown -R www:www %s' % remote_dist_link)
    # 删除旧的连接
    run('rm -f %s' % remote_dist_link)
    # 创建新的软链接指向新部署的目录：
    run('ln -s %s %s' % (remote_dist_dir, remote_dist_link))
    run('chown -R www:www %s' % remote_dist_link)

    # 重启服务后台API
    # supervisord='/etc/supervisorctl restart'
    # with settings(warn_only=True):
    #     for index in xrange(len(api_server)):
    #           run('%s restart' % api_server[index] )

###################################
##不直接写明文到程序文件中，通过ssh认证方式
#取得开发本地sshkey
# @roles('developer')
# def get_sshkey():
#     get('/root/.ssh/id_rsa.pub', 'id_rsa.pub.master')
#
# #上传sshkey到嗠器
# @roles('deployserver')
# def put_sshkey():
#     with cd('/tmp'):
#         put('id_rsa.pub.master', 'id_rsa.pub.master')
#         run('cat id_rsa.pub.master >> /root/.ssh/authorized_keys')
####################################
#定义一个DeployServer部署任务
@roles('deploysvr')
def deploysvr():
    'DeployServer部署任务'
    global remote_sdist_dir
    remote_tmp_tar = '/tmp/x-python-svr.tar.gz'
    remote_dist_link = '/var/www_dev'
    if not remote_dist_link:
        sudo('touch %s',remote_dist_link)
    tag = datetime.now().strftime('%y.%m.%d.%H.%M.%S')
    # delete old remote_dir
    if remote_sdist_dir:
        sudo('rm -rf %s' % remote_sdist_dir,user=sudouser)
    sudo('rm -f %s' % remote_tmp_tar,user=sudouser)
    # 解压:
    remote_sdist_dir = '/var/www.x-python-svr.com@%s' % tag
    put('x-python-svr.tar.gz', remote_tmp_tar)
    sudo('mkdir %s' % remote_sdist_dir,user=sudouser)
    with cd(remote_sdist_dir):
        sudo('tar -xzvf %s' % remote_tmp_tar,user=sudouser)
        # 设置新目录的www-data权限
    sudo('chown -R www:www %s' % remote_dist_link,user=sudouser)
    # 删除旧的连接
    sudo('rm -f %s' % remote_dist_link,user=sudouser)
    # 创建新的软链接指向新部署的目录：
    sudo('ln -s %s %s' % (remote_sdist_dir, remote_dist_link),user=sudouser)
    sudo('chown -R www:www %s' % remote_dist_link,user=sudouser)


def deploy():
    execute(deploytest)
    #exec()
#错误处理


# def do_ssj_conn():
#     execute(get_sshkey)
#     execute(put_sshkey)

def test():
    pass
